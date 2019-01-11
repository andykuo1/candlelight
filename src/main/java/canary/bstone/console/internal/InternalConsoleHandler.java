package canary.bstone.console.internal;

import canary.bstone.console.Console;
import canary.bstone.console.ConsoleCommand;
import canary.bstone.console.command.CommandException;
import canary.bstone.console.style.ConsoleStyle;

/**
 * Created by Andy on 12/6/17.
 */
public class InternalConsoleHandler implements InternalConsoleListener
{
	Console console;

	public InternalConsoleHandler(Console console)
	{
		this.console = console;
		ConsoleCommand command = this.console.command();
		command.registerCommand("light", args -> {
			if (args.length != 0) throw new CommandException("expected no args");
			ConsoleStyle.setLightMode(this.console);
		});
		command.registerCommand("dark", args -> {
			if (args.length != 0) throw new CommandException("expected no args");
			ConsoleStyle.setDarkMode(this.console);
		});
		command.registerCommand("delay", args -> {
			if (args.length == 0)
			{
				this.console.writer().setTypeDelay(1);
			}
			else if (args.length == 1)
			{
				this.console.writer().setTypeDelay(Double.parseDouble(args[0]));
			}
			else
			{
				throw new CommandException("expected 0-1 args");
			}
		});
		command.registerCommand("nodelay", args -> {
			if (args.length != 0) throw new CommandException("expected no args");
			this.console.writer().setTypeDelay(0);
		});
		command.registerCommand("quit", args -> {
			if (args.length != 0) throw new CommandException("expected no args");
			this.console.stop();
		});
	}

	public void doLoopSleep(int time)
	{
		this.update();

		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
		}
	}

	public void update()
	{
		if (!this.console.isRunning())
		{
			this.console.getFrame().dispose();
			throw new ConsoleCloseException();
		}
	}

	public void resetCaret()
	{
		this.console.getComponent().setCaretPosition(this.console.writer().getDocument().getLength());
	}

	@Override
	public void onWriterNewLine()
	{
		this.resetCaret();
		this.update();
	}

	@Override
	public void onWriterDeleteLine(String text)
	{
		this.resetCaret();
		this.update();
	}

	@Override
	public void onInputExecuteEvent(String input)
	{
		if (input.length() > 1 && input.charAt(0) == ConsoleCommand.COMMAND_CHAR)
		{
			this.console.input().poll();

			try
			{
				this.console.command().processCommand(input.substring(1).trim());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
