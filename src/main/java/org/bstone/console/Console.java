package org.bstone.console;

import org.bstone.console.internal.ConsoleCloseException;
import org.bstone.console.internal.InternalConsoleHandler;
import org.bstone.console.program.ActionLink;
import org.bstone.console.program.ActionLinkEvent;
import org.bstone.console.program.ConsoleBoard;
import org.bstone.console.program.ConsoleProgram;
import org.bstone.console.style.StyleBuilder;
import org.qsilver.ResourceLocation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

/**
 * Created by Andy on 12/3/17.
 */
public class Console
{
	private final ConsoleInput input;
	private final ConsoleWriter writer;
	private final ConsoleCommand command;

	private final InternalConsoleHandler handler;

	private final Queue<ActionLinkEvent> actionEvents = new LinkedBlockingQueue<>();
	private volatile boolean canActionEvent = false;

	private final JFrame frame;
	private final JTextPane pane;

	private final StyleBuilder defaultStyle;
	private final StyleBuilder errorStyle;
	private final StyleBuilder infoStyle;
	private final StyleBuilder linkStyle;

	private volatile boolean running = false;

	private final ConsoleBoard board;

	public Console(String name)
	{
		//Initialize console frame

		final Font font = getDefaultFont();
		final JFrame frame = createJavaFrame(name, 640);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Console.this.stop();
			}
		});

		//Initialize console content

		JTextPane pane;
		JTextField field;

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		{
			pane = new JTextPane();
			{
				pane.setBorder(new EmptyBorder(10, 10, 10, 10));
				pane.setEditable(false);
				pane.setFont(font);
				pane.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseReleased(MouseEvent e)
					{
						if (!Console.this.click(e.getPoint())) Console.this.focus();
					}
				});

				JScrollPane scrollPane = new JScrollPane(pane,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
				scrollPane.getVerticalScrollBar().setUnitIncrement(5);
				scrollPane.setBorder(null);

				panel.add(scrollPane, BorderLayout.CENTER);
			}

			field = new JTextField();
			{
				field.setBorder(new EmptyBorder(10, 10, 10, 10));
				field.setFont(font);

				panel.add(field, BorderLayout.SOUTH);
			}
		}
		frame.setContentPane(panel);

		frame.pack();
		frame.setVisible(true);
		field.requestFocus();

		this.frame = frame;
		this.pane = pane;

		//Initialize console internal components

		this.input = new ConsoleInput(field);
		this.writer = new ConsoleWriter(this.pane.getStyledDocument());
		this.command = new ConsoleCommand();
		this.handler = new InternalConsoleHandler(this);
		this.writer.setListener(this.handler);
		this.input.setListener(this.handler);

		//Initialize console styles

		this.defaultStyle = new StyleBuilder(this.writer);
		this.errorStyle = new StyleBuilder(this.defaultStyle).color(Color.RED).setFormatter(o -> "ERROR: " + o);
		this.infoStyle = new StyleBuilder(this.defaultStyle).color(Color.LIGHT_GRAY).setFormatter(o -> "[" + o + "]");
		this.linkStyle = new StyleBuilder(this.defaultStyle).color(Color.BLUE).underline(true);

		this.board = new ConsoleBoard();
	}

	//******************************
	//  Console program management
	//******************************

	public void start(ConsoleProgram program)
	{
		this.running = true;
		try
		{
			this.board.next(this, program);

			while (this.running)
			{
				this.waitForNewActionEvent();
			}
			this.running = false;
		}
		catch (ConsoleCloseException e)
		{
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void stop()
	{
		this.running = false;
	}

	//******************************
	//  Basic console functions
	//******************************

	public synchronized String waitForContinue()
	{
		this.input.flush();
		this.focus();
		this.info("'ENTER' to continue");
		return this.waitForResponse(s -> true);
	}

	public synchronized String waitForNewResponse(Function<String, Boolean> validator)
	{
		this.input.flush();
		return this.waitForResponse(validator);
	}

	public synchronized String waitForResponse(Function<String, Boolean> validator)
	{
		this.focus();

		String response;
		while ((response = this.input.poll()) == null || !validator.apply(response))
		{
			this.handler.doLoopSleep(10);
		}
		return response;
	}

	public synchronized void waitForNewActionEvent()
	{
		boolean prev = this.canActionEvent;
		this.canActionEvent = true;
		{
			ActionLinkEvent e;
			while((e = this.actionEvents.poll()) == null)
			{
				this.handler.doLoopSleep(100);
			}

			this.canActionEvent = false;
			e.action.execute(e);
		}
		this.canActionEvent = prev;
	}

	public synchronized void sleep(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
		}
	}

	public synchronized StyleBuilder style()
	{
		return new StyleBuilder(this.defaultStyle);
	}

	public synchronized void link(Object msg, ActionLink action)
	{
		new StyleBuilder(this.linkStyle).action(action).format(msg);
	}

	public synchronized void println(Object msg)
	{
		this.defaultStyle.formatln(msg);
	}

	public synchronized void println()
	{
		this.writer.newline();
	}

	public synchronized void print(Object msg)
	{
		this.defaultStyle.format(msg);
	}

	public synchronized void deleteln(int lines)
	{
		this.writer.deleteLines(lines);
	}

	public synchronized void deleteln()
	{
		this.deleteln(1);
	}

	public synchronized void delete(int chars)
	{
		this.writer.deleteChars(chars);
	}

	public synchronized void info(Object msg)
	{
		this.infoStyle.formatln(msg);
	}

	public synchronized void error(Object msg)
	{
		this.errorStyle.formatln(msg);
	}

	public synchronized void clear()
	{
		this.writer.deleteAll();
	}

	public synchronized void flush()
	{
		this.input.flush();

		while (!this.actionEvents.isEmpty())
		{
			ActionLinkEvent e = this.actionEvents.poll();
			e.action.execute(e);
		}
	}

	//******************************
	//  Internal console components
	//******************************

	public ConsoleInput input()
	{
		return this.input;
	}

	public ConsoleWriter writer()
	{
		return this.writer;
	}

	public ConsoleCommand command()
	{
		return this.command;
	}

	public ConsoleBoard board()
	{
		return this.board;
	}

	public boolean isRunning()
	{
		return this.running;
	}

	//******************************
	//  Basic console styles
	//******************************

	public StyleBuilder getDefaultStyle()
	{
		return this.defaultStyle;
	}

	public StyleBuilder getErrorStyle()
	{
		return this.errorStyle;
	}

	public StyleBuilder getInfoStyle()
	{
		return this.infoStyle;
	}

	public StyleBuilder getLinkStyle()
	{
		return this.linkStyle;
	}

	//******************************
	//  Java components
	//******************************

	public JFrame getFrame()
	{
		return this.frame;
	}

	public JTextPane getComponent()
	{
		return this.pane;
	}

	//******************************
	//  Threaded functions (caution)
	//******************************

	private boolean click(Point point)
	{
		if (!this.canActionEvent) return false;

		point.x -= 0.5F;
		int index = this.pane.viewToModel(point);

		if (index == -1) return false;

		StyledDocument doc = this.pane.getStyledDocument();
		Element element = doc.getCharacterElement(index);

		ActionLink action = ActionLink.getActionFromAttributes(element.getAttributes());

		if (action != null)
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
			if (string == null) return false;

			ActionLinkEvent e = new ActionLinkEvent();
			e.console = this;
			e.action = action;
			e.document = doc;
			e.beginIndex = begin;
			e.endIndex = end;
			e.element = element;
			this.actionEvents.offer(e);
		}
		else
		{
			return false;
		}

		return true;
	}

	private void focus()
	{
		this.input.getComponent().grabFocus();
	}

	private static Font defaultFont;
	private static Font getDefaultFont()
	{
		if (defaultFont == null)
		{
			try
			{
				defaultFont = Font.createFont(Font.PLAIN, new File(new ResourceLocation("base:console.ttf").getFilePath())).deriveFont(12F);
			}
			catch (FontFormatException | IOException e)
			{
				e.printStackTrace();
			}
		}
		return defaultFont;
	}

	private static JFrame createJavaFrame(String name, int height)
	{
		final Dimension min = new Dimension(20 + 400, 20 + 100);
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		JFrame frame = new JFrame();
		frame.setTitle(name);
		frame.setSize(min.width, height);
		frame.setMaximumSize(new Dimension(min.width, screen.height));
		frame.setPreferredSize(new Dimension(min.width, height));
		frame.setMinimumSize(min);
		frame.setLocation(screen.width / 2 - min.width / 2, screen.height / 2 - height / 2);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		return frame;
	}
}
