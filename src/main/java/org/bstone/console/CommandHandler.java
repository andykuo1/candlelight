package org.bstone.console;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Andy on 10/7/17.
 */
public class CommandHandler
{
	private final Map<String, Consumer<String[]>> commands = new HashMap<>();

	public void addCommand(String cmd, Consumer<String[]> handler)
	{
		this.commands.put(cmd, handler);
	}

	public Consumer<String[]> removeCommand(String cmd)
	{
		return this.commands.remove(cmd);
	}

	public void processCommand(String text) throws Exception
	{
		String cmd;
		String[] args;

		final int i = text.indexOf(" ");
		cmd = i == -1 ? text.substring(1) : text.substring(1, i);
		args = i == -1 ? new String[0] : text.substring(i + 1).split(" ");

		Consumer<String[]> command = this.commands.get(cmd.toLowerCase());
		if (command == null)
		{
			command = this.commands.get(null);
		}

		if (command != null)
		{
			command.accept(args);
		}
	}

	public Consumer<String[]> getCommand(String command)
	{
		return this.commands.get(command);
	}

	public Iterable<String> getCommands()
	{
		return this.commands.keySet();
	}
}
