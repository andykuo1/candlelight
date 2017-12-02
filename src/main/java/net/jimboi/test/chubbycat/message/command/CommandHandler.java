package net.jimboi.test.chubbycat.message.command;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Andy on 11/29/17.
 */
public abstract class CommandHandler
{
	private final String[] names;

	public CommandHandler(String... names)
	{
		this.names = names;
	}

	public abstract boolean process(ChannelHandlerContext ctx, String name, String... args);

	public abstract String getUsageInfo();

	public String getUsageCommand()
	{
		return this.getCommandNames()[0];
	}

	public final String[] getCommandNames()
	{
		return this.names;
	}
}
