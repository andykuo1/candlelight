package canary.bstone.console;

import canary.bstone.console.internal.InternalConsoleListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import javax.swing.JTextField;

/**
 * Created by Andy on 12/3/17.
 */
public class ConsoleInput
{
	private final Queue<String> responses = new LinkedBlockingQueue<>();
	private final LinkedList<String> history = new LinkedList<>();
	private int historyIndex = -1;

	private final Queue<String> commands = new LinkedBlockingQueue<>();

	private InternalConsoleListener listener;

	private final JTextField field;

	public ConsoleInput(JTextField field)
	{
		this.field = field;

		this.field.addActionListener(e -> {
			String text = this.field.getText();
			this.field.setText("");
			this.offer(text);

			ConsoleInput.this.listener.onInputExecuteEvent(text);
		});

		this.field.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_UP)
				{
					ConsoleInput.this.previousHistory();
				}
				else if (key == KeyEvent.VK_DOWN)
				{
					ConsoleInput.this.nextHistory();
				}

				ConsoleInput.this.listener.onInputKeyEvent(key);
			}
		});
	}

	public ConsoleInput setListener(InternalConsoleListener listener)
	{
		this.listener = listener;
		return this;
	}

	public void offer(String input)
	{
		this.responses.offer(input);
		this.history.addFirst(input);
		this.historyIndex = -1;
	}

	public String poll()
	{
		if (!this.responses.isEmpty())
		{
			return this.responses.poll();
		}
		else
		{
			return null;
		}
	}

	public void flush(Consumer<String> action)
	{
		while(!this.responses.isEmpty())
		{
			String response = this.responses.poll();
			action.accept(response);
		}
	}

	public void flush()
	{
		this.responses.clear();
	}

	public void setCurrentText(String text)
	{
		this.field.setText(text);
		this.field.grabFocus();
	}

	public String getCurrentText()
	{
		return this.field.getText();
	}

	public void setEnabled(boolean enabled)
	{
		this.field.setEnabled(enabled);
	}

	public boolean isEnabled()
	{
		return this.field.isEnabled();
	}

	//******************************
	//  Java components
	//******************************

	public JTextField getComponent()
	{
		return this.field;
	}

	//******************************
	//  Threaded functions (caution)
	//******************************

	private void nextHistory()
	{
		if (this.history.isEmpty()) return;

		--this.historyIndex;

		if (this.historyIndex < 0)
		{
			this.historyIndex = -1;
			this.field.setText("");
		}
		else
		{
			this.field.setText(this.history.get(this.historyIndex));
		}
	}

	private void previousHistory()
	{
		if (this.history.isEmpty()) return;

		++this.historyIndex;

		if (this.historyIndex >= this.history.size())
		{
			this.historyIndex = this.history.size() - 1;
		}

		this.field.setText(this.history.get(this.historyIndex));
	}
}
