package net.jimboi.test.chubbycat.message.command;

import net.jimboi.test.chubbycat.server.ServerHandler;

import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Andy on 11/29/17.
 */
public class CommandNickname extends CommandHandler
{
	public CommandNickname()
	{
		super("nick", "nickname");
	}

	@Override
	public boolean process(ChannelHandlerContext ctx, String name, String... args)
	{
		if (args.length == 1)
		{
			String newName = args[0];
			for(String s : ServerHandler.NAMES.values())
			{
				if (newName.equals(s)) return false;
			}

			ServerHandler.NAMES.put(ctx.channel(), newName);
			return true;
		}
		else if (args.length == 2)
		{
			Channel ch = null;
			String user = args[0];
			String newName = args[1];
			for(Map.Entry<Channel, String> entry : ServerHandler.NAMES.entrySet())
			{
				if (entry.getValue().equals(user))
				{
					ch = entry.getKey();
					break;
				}
			}
			if (ch == null) return false;
			ServerHandler.NAMES.put(ch, newName);
			return true;
		}

		return false;
	}

	@Override
	public String getUsageInfo()
	{
		return "Change the name of the specified user or yourself";
	}

	@Override
	public String getUsageCommand()
	{
		return super.getUsageCommand() + " [OLD_NAME] [NEW_NAME] or" +
				super.getUsageCommand() + " [NEW_NAME]";
	}
}
