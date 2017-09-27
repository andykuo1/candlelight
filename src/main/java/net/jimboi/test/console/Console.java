package net.jimboi.test.console;

import org.zilar.resource.ResourceLocation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Created by Andy on 8/22/17.
 */
public class Console
{
	private static final String LINK_HANDLER_KEY = "linkhandler";
	private static boolean DEBUG = true;

	private final JFrame frame;
	private final JPanel content;
	private final JTextPane text;
	private final JTextField field;

	private final Map<String, Consumer<String[]>> commandHandlers = new HashMap<>();
	{
		this.setDefaultCommandHandler(strings -> this.error("Invalid command."));
	}
	private Predicate<Console> windowCloseHandler;

	private Consumer<String> fieldHandler = this::println;
	private LinkedList<String> fieldHistory = new LinkedList<>();
	private int fieldHistoryIndex = 0;

	public Console(int width, int height)
	{
		Font font = null;
		try
		{
			font = Font.createFont(Font.PLAIN, new File(new ResourceLocation("base:console.ttf").getFilePath())).deriveFont(12F);
		}
		catch(FontFormatException | IOException e)
		{
			e.printStackTrace();
		}

		this.frame = new JFrame();
		this.frame.setSize(width, height);
		this.frame.setPreferredSize(new Dimension(width, height));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (Console.this.windowCloseHandler == null || Console.this.windowCloseHandler.test(Console.this))
				{
					Console.this.destroy();
				}
			}
		});

		this.field = new JTextField();
		this.content = new JPanel();
		{
			this.content.setLayout(new BorderLayout());

			this.text = new JTextPane();
			this.text.setBorder(new EmptyBorder(10, 10, 10, 10));
			this.text.setBackground(Color.WHITE);
			this.text.setForeground(Color.BLACK);
			this.text.setEditable(false);
			this.text.setFont(font);
			this.text.addMouseListener(new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e) {}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e)
				{
					Point mousePoint = e.getPoint();
					mousePoint.x -= 0.5F;
					int index = Console.this.text.viewToModel(mousePoint);

					if (index == -1) return;

					StyledDocument doc = Console.this.getDocument();
					Element element = doc.getCharacterElement(index);

					LinkHandler handler = (LinkHandler) element.getAttributes().getAttribute(LINK_HANDLER_KEY);

					if (handler != null)
					{
						int begin = element.getStartOffset();
						int end = element.getEndOffset();
						String string = null;
						try
						{
							string = doc.getText(begin, end - begin);
						}
						catch (BadLocationException ex)
						{
							ex.printStackTrace();
						}

						if (string == null) return;

						handler.execute(Console.this, doc, string, begin, end, element);
					}
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{

				}

				@Override
				public void mouseExited(MouseEvent e)
				{

				}
			});

			final JScrollPane scrollPane = new JScrollPane(this.text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.content.add(scrollPane, BorderLayout.CENTER);

			this.field.setBorder(new EmptyBorder(10, 10, 10, 10));
			this.field.setFont(font);
			this.field.addKeyListener(new KeyListener()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
				}

				@Override
				public void keyPressed(KeyEvent e)
				{
					if (fieldHistory.isEmpty()) return;

					if (e.getKeyCode() == KeyEvent.VK_UP)
					{
						fieldHistoryIndex++;

						if (fieldHistoryIndex >= fieldHistory.size())
						{
							fieldHistoryIndex = fieldHistory.size() - 1;
						}

						field.setText(fieldHistory.get(fieldHistoryIndex));
					}
					else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					{
						fieldHistoryIndex--;

						if (fieldHistoryIndex < 0)
						{
							field.setText("");
							fieldHistoryIndex = -1;
						}
						else
						{
							field.setText(fieldHistory.get(fieldHistoryIndex));
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e)
				{
				}
			});
			this.field.addActionListener(e ->
			{
				String text = this.field.getText();
				this.fieldHistory.addFirst(text);
				this.fieldHistoryIndex = -1;

				if (text.startsWith("/"))
				{
					this.processCommand(text);
				}
				else
				{
					Console.this.fieldHandler.accept(this.field.getText());
				}

				this.field.setText("");
			});
			this.content.add(this.field, BorderLayout.SOUTH);
		}
		this.frame.setContentPane(this.content);

		this.frame.pack();
		this.frame.setVisible(true);

		this.field.requestFocus();
	}

	public void destroy()
	{
		this.frame.dispose();
	}

	public void processCommand(String text)
	{
		String cmd;
		String[] args;

		final int i = text.indexOf(" ");
		cmd = i == -1 ? text.substring(1) : text.substring(1, i);
		args = i == -1 ? new String[0] : text.substring(i + 1).split(" ");

		Consumer<String[]> command = Console.this.commandHandlers.get(cmd.toLowerCase());
		if (command == null)
		{
			command = Console.this.commandHandlers.get(null);
		}

		if (command != null)
		{
			try
			{
				command.accept(args);
			}
			catch (Exception e)
			{
				this.error("Command failed.");
			}
		}
	}

	public void addCommandHandler(String cmd, Consumer<String[]> handler)
	{
		this.commandHandlers.put(cmd, handler);
	}

	public Consumer<String[]> removeCommandHandler(String cmd)
	{
		return this.commandHandlers.remove(cmd);
	}

	public void setDefaultCommandHandler(Consumer<String[]> handler)
	{
		this.commandHandlers.put(null, handler);
	}

	public void setWindowCloseHandler(Predicate<Console> windowCloseHandler)
	{
		this.windowCloseHandler = windowCloseHandler;
	}

	public void setInputTextHandler(Consumer<String> inputTextHandler)
	{
		this.fieldHandler = inputTextHandler;
	}

	public TextAttribute beginAttributes()
	{
		return new TextAttribute(this);
	}

	public synchronized void error(String msg)
	{
		this.beginAttributes().setColor(Color.RED).printlnEnd("ERROR: " + msg);
	}

	public synchronized Console print(String msg)
	{
		this.print(msg, null);
		return this;
	}

	public synchronized Console println(String msg)
	{
		this.print(msg, null);
		this.print("\n", null);
		return this;
	}

	public synchronized Console println()
	{
		this.print("\n", null);
		return this;
	}

	protected void print(String msg, javax.swing.text.AttributeSet attrib)
	{
		if (DEBUG) System.out.print(msg);

		StyledDocument doc = this.text.getStyledDocument();

		try
		{
			doc.insertString(doc.getLength(), msg, attrib);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		this.text.setCaretPosition(doc.getLength());
	}

	public void clear()
	{
		this.text.setText("");
	}

	public void setInput(String input)
	{
		this.field.setText(input);
	}

	public void setTitle(String title)
	{
		this.frame.setTitle(title);
	}

	public final String getTitle()
	{
		return this.frame.getTitle();
	}

	public StyledDocument getDocument()
	{
		return this.text.getStyledDocument();
	}

	public Consumer<String[]> getCommand(String command)
	{
		return this.commandHandlers.get(command);
	}

	public Iterable<String> getCommands()
	{
		return this.commandHandlers.keySet();
	}

	@FunctionalInterface
	public interface LinkHandler
	{
		void execute(Console console, StyledDocument document, String text, int beginIndex, int endIndex, Element element);
	}

	public final class TextAttribute extends SimpleAttributeSet
	{
		private final Console console;

		private TextAttribute(Console console)
		{
			this.console = console;

			StyleConstants.setLineSpacing(this, 16F);
		}

		public TextAttribute setUnderline(boolean underline)
		{
			StyleConstants.setUnderline(this, underline);
			return this;
		}

		public TextAttribute setColor(Color color)
		{
			StyleConstants.setForeground(this, color);
			return this;
		}

		public TextAttribute setActionLink(Consumer<Console> action)
		{
			this.addAttribute(LINK_HANDLER_KEY, (LinkHandler) (console, document, text, beginIndex, endIndex, element) -> action.accept(console));
			this.setColor(Color.BLUE);
			this.setUnderline(true);
			return this;
		}

		public TextAttribute setCustomLink(LinkHandler handler)
		{
			this.addAttribute(LINK_HANDLER_KEY, handler);
			return this;
		}

		public synchronized Console printEnd(String msg)
		{
			this.console.print(msg, this);
			return this.console;
		}

		public synchronized Console printlnEnd(String msg)
		{
			this.console.print(msg, this);
			this.console.print("\n", null);
			return this.console;
		}
	}
}
