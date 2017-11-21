package net.jimboi.test.multiuser.nettypacket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 8/15/17.
 */
public class CommandManager
{
	private final Map<String, Command> commands = new HashMap<>();

	public void register(String name, Command command)
	{
		this.commands.put(name, command);
	}

	public Command remove(String name)
	{
		return this.commands.remove(name);
	}

	public Command getCommand(String name)
	{
		return this.commands.get(name);
	}
}
