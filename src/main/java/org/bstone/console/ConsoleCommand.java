package org.bstone.console;

import org.bstone.console.command.Command;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 12/6/17.
 */
public class ConsoleCommand
{
	public static final char COMMAND_CHAR = '~';

	private final Map<String, Command> commands = new HashMap<>();

	public void registerCommand(String cmd, Command handler)
	{
		this.commands.put(cmd, handler);
	}

	public Command unregisterCommand(String cmd)
	{
		return this.commands.remove(cmd);
	}

	public void processCommand(String text) throws Exception
	{
		String cmd;
		String[] args;

		final int i = text.indexOf(" ");
		cmd = i == -1 ? text : text.substring(0, i);
		args = i == -1 ? new String[0] : text.substring(i + 1).split(" ");

		Command command = this.commands.get(cmd.toLowerCase());
		if (command == null)
		{
			command = this.commands.get(null);
		}

		if (command != null)
		{
			try
			{
				command.process(args);
			}
			catch (Exception e)
			{
				Toolkit.getDefaultToolkit().beep();
				System.err.println("COMMAND ERROR: " + e.getMessage());
				System.err.println("COMMAND USAGE: " + COMMAND_CHAR + cmd.toLowerCase() + " " + command.getUsage());
			}
		}
		else
		{
			Toolkit.getDefaultToolkit().beep();
			System.err.println("COMMAND ERROR: invalid command");
		}
	}

	public Command getCommand(String command)
	{
		return this.commands.get(command);
	}

	public Iterable<String> getCommands()
	{
		return this.commands.keySet();
	}
}
