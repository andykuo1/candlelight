package org.bstone.console;

import org.zilar.resource.ResourceLocation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
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
public class Console extends CommandHandler implements Runnable
{
	private static boolean DEBUG = true;

	public static void setLightMode(Console console)
	{
		Color back = new Color(0xFFFFFF);
		Color back2 = new Color(0xEEEEEE);
		Color fore = new Color(0x000000);
		Color link = new Color(0x6673D2);
		Color error = new Color(0xCF3B40);

		console.setInputFieldColor(fore, back2);
		console.setWindowColor(fore, back);
		console.setLinkColor(link);
		console.setErrorColor(error);
	}

	public static void setDarkMode(Console console)
	{
		Color back = new Color(0x1F1F1F);
		Color back2 = new Color(0x292929);
		Color fore = new Color(0xFFFFFF);
		Color link = new Color(0x798FBA);
		Color error = new Color(0xB67070);

		console.setInputFieldColor(fore, back2);
		console.setWindowColor(fore, back);
		console.setLinkColor(link);
		console.setErrorColor(error);
	}

	protected final JFrame frame;
	protected final JPanel panel;
	protected final JTextPane textContent;
	protected final JTextField inputField;

	private final EventAdapter adapter = new EventAdapter(this);
	private final Queue<Event> events = new LinkedBlockingQueue<>();

	private final int maxLength = 40;

	private Predicate<Console> windowCloseHandler;
	protected volatile boolean running = false;

	private Consumer<String> inputHandler = this::println;
	private LinkedList<String> inputHistory = new LinkedList<>();
	private int inputHistoryIndex = 0;

	private Color errorColor = Color.RED;
	private Color linkColor = Color.BLUE;

	private boolean typeWriter = true;
	private Function<Character, Integer> typeWriterDelayHandler =
			c -> c == '.' || c == '?' || c == '!' ? 200 : c == ',' ? 100 : 30;

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
		this.frame.setMinimumSize(new Dimension(20 + this.maxLength * 10, 20 + 100));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(this.adapter);

		this.inputField = new JTextField();
		this.panel = new JPanel();
		{
			this.panel.setLayout(new BorderLayout());

			this.textContent = new JTextPane();
			this.textContent.setBorder(new EmptyBorder(10, 10, 10, 10));
			this.textContent.setEditable(false);
			this.textContent.setFont(font);
			this.textContent.addMouseListener(this.adapter);

			JScrollPane scrollPane = new JScrollPane(this.textContent,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
			scrollPane.setBorder(null);

			this.panel.add(scrollPane, BorderLayout.CENTER);

			this.inputField.setBorder(new EmptyBorder(10, 10, 10, 10));
			this.inputField.setFont(font);
			this.inputField.addKeyListener(this.adapter);
			this.inputField.addActionListener(this.adapter);

			this.panel.add(this.inputField, BorderLayout.SOUTH);
		}
		this.frame.setContentPane(this.panel);

		this.frame.pack();
		this.frame.setVisible(true);

		this.inputField.requestFocus();

		this.addCommand(null, args -> this.error("Invalid command."));
		this.addCommand("clear", args -> this.clear());
		this.addCommand("exit", args -> this.quit());
		this.addCommand("typewriter", args -> this.setTypeWriterMode(Boolean.parseBoolean(args[0])));
	}

	public void destroy()
	{
		this.frame.dispose();
		this.running = false;
	}

	@Override
	public void run()
	{
		long maxDelta = 100;
		this.running = true;
		while (this.running)
		{
			long prev = System.currentTimeMillis();
			long next = prev;
			while (!this.events.isEmpty() && next - prev < maxDelta)
			{
				Event event = this.events.remove();
				event.execute();
				next = System.currentTimeMillis();
			}

			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	public Console setWindowCloseHandler(Predicate<Console> windowCloseHandler)
	{
		this.windowCloseHandler = windowCloseHandler;
		return this;
	}

	public Console setInputFieldHandler(Consumer<String> inputTextHandler)
	{
		this.inputHandler = inputTextHandler;
		return this;
	}

	public Console setInputFieldColor(Color foreground, Color background)
	{
		this.inputField.setBackground(background);
		this.inputField.setForeground(foreground);
		this.inputField.setCaretColor(foreground);
		return this;
	}

	public Console setWindowColor(Color foreground, Color background)
	{
		this.textContent.setBackground(background);
		this.textContent.setForeground(foreground);
		this.textContent.setCaretColor(foreground);
		return this;
	}

	public Console setLinkColor(Color color)
	{
		this.linkColor = color;
		return this;
	}

	public Console setErrorColor(Color color)
	{
		this.errorColor = color;
		return this;
	}

	public Console setTypeWriterMode(boolean enabled)
	{
		this.typeWriter = enabled;
		return this;
	}

	public Console setTypeWriterDelayHandler(Function<Character, Integer> typeWriterDelayHandler)
	{
		this.typeWriterDelayHandler = typeWriterDelayHandler;
		return this;
	}

	public Console setInput(String input)
	{
		this.inputField.setText(input);
		return this;
	}

	public Console setTitle(String title)
	{
		this.frame.setTitle(title);
		return this;
	}

	public void scrollTo(int lineNumber)
	{
		int index = this.textContent.getCaretPosition();
		for(int i = 0; i < lineNumber; ++i)
		{
			index = this.textContent.getText().indexOf('\n', index + 1);
		}
		this.textContent.setCaretPosition(index);
	}

	public void quit()
	{
		if (this.windowCloseHandler == null || this.windowCloseHandler.test(Console.this))
		{
			this.destroy();
		}
	}

	public <E> void fireEvent(Consumer<E> consumer, E argument)
	{
		this.events.offer(new Event<>(consumer, argument));
	}

	public TextAttribute beginAttributes()
	{
		return new TextAttribute(this);
	}

	public synchronized void error(String msg)
	{
		this.beginAttributes().setColor(this.errorColor).printlnEnd("ERROR: " + msg);
	}

	public synchronized Console print(String msg, Color color)
	{
		SimpleAttributeSet attrib = new SimpleAttributeSet();
		StyleConstants.setForeground(attrib, color);
		this.print(msg, attrib);
		return this;
	}

	public synchronized Console println(String msg, Color color)
	{
		this.print(msg, color);
		this.println();
		return this;
	}

	public synchronized Console print(String msg)
	{
		this.print(msg, (javax.swing.text.AttributeSet) null);
		return this;
	}

	public synchronized Console println(String msg)
	{
		this.print(msg);
		this.println();
		return this;
	}

	public synchronized Console println()
	{
		this.print("\n", (javax.swing.text.AttributeSet) null);
		return this;
	}

	protected void print(String msg, javax.swing.text.AttributeSet attrib)
	{
		if (DEBUG) System.out.print(msg);

		String text = this.textContent.getText();
		int remaining = this.maxLength - (text.length() - text.lastIndexOf('\n'));

		int i = msg.indexOf('\n');
		if (i == -1) i = msg.length();

		if (i > remaining)
		{
			this.println(msg.substring(0, remaining));
			this.print(msg.substring(remaining));
			return;
		}

		StyledDocument doc = this.textContent.getStyledDocument();

		if (this.typeWriter && !(attrib instanceof TextAttribute))
		{
			this.inputField.setEnabled(false);
			for(char c : msg.toCharArray())
			{
				this.append(doc, "" + c, attrib);

				try
				{
					Thread.sleep(this.typeWriterDelayHandler.apply(c));
				}
				catch (InterruptedException e)
				{
				}
			}
			this.inputField.setEnabled(true);
			this.inputField.requestFocus();
		}
		else
		{
			this.append(doc, msg, attrib);
		}

		this.textContent.setCaretPosition(doc.getLength());
	}

	private void append(StyledDocument doc, String msg, javax.swing.text.AttributeSet attrib)
	{
		try
		{
			doc.insertString(doc.getLength(), msg, attrib);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	public void clear()
	{
		this.textContent.setText("");
	}

	public int getMaxLength()
	{
		return this.maxLength;
	}

	public Color getErrorColor()
	{
		return this.errorColor;
	}

	public Color getLinkColor()
	{
		return this.linkColor;
	}

	public boolean isTypeWriterMode()
	{
		return this.typeWriter;
	}

	public String getTitle()
	{
		return this.frame.getTitle();
	}

	public StyledDocument getDocument()
	{
		return this.textContent.getStyledDocument();
	}

	protected void onWindowCloseEvent(WindowEvent e)
	{
		this.quit();
	}

	protected void onContentMouseClickedEvent(MouseEvent e)
	{
		this.inputField.requestFocus();
	}

	protected void onContentMouseReleasedEvent(MouseEvent e)
	{
		Point mousePoint = e.getPoint();
		mousePoint.x -= 0.5F;
		int index = this.textContent.viewToModel(mousePoint);

		if (index == -1) return;

		StyledDocument doc = this.getDocument();
		Element element = doc.getCharacterElement(index);

		LinkHandler handler = LinkHandler.getLinkHandler(element.getAttributes());

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

			handler.execute(this, doc, string, begin, end, element);
		}
	}

	protected void onInputFieldKeyPressedEvent(KeyEvent e)
	{
		if (this.inputHistory.isEmpty()) return;

		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			this.inputHistoryIndex++;

			if (this.inputHistoryIndex >= this.inputHistory.size())
			{
				this.inputHistoryIndex = this.inputHistory.size() - 1;
			}

			this.inputField.setText(this.inputHistory.get(this.inputHistoryIndex));
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			this.inputHistoryIndex--;

			if (this.inputHistoryIndex < 0)
			{
				this.inputField.setText("");
				this.inputHistoryIndex = -1;
			}
			else
			{
				this.inputField.setText(this.inputHistory.get(this.inputHistoryIndex));
			}
		}
	}

	protected void onInputFieldActionEvent(ActionEvent e)
	{
		String text = this.inputField.getText();
		this.inputHistory.addFirst(text);
		this.inputHistoryIndex = -1;

		if (text.startsWith("/"))
		{
			try
			{
				this.processCommand(text);
			}
			catch (Exception ex)
			{
				this.error("Command failed - " + ex.getMessage());
			}
		}
		else
		{
			this.inputHandler.accept(this.inputField.getText());
		}

		this.inputField.setText("");
	}

}
