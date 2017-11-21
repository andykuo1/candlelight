package net.jimboi.test.multiuser.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by Andy on 8/14/17.
 */
public class ChatServerHandler extends ChannelInboundHandlerAdapter
{
	public static final ChannelGroup CHANNELS = new DefaultChannelGroup("channels", GlobalEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[SERVER] : " + incoming.remoteAddress() + " has joined!";

		System.out.println(output);

		for(Channel channel : CHANNELS)
		{
			channel.writeAndFlush(output + "\n");
		}

		CHANNELS.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[SERVER] : " + incoming.remoteAddress() + " has left!";

		System.out.println(output);

		for(Channel channel : CHANNELS)
		{
			channel.write(output + "\n");
		}

		CHANNELS.remove(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[" + incoming.remoteAddress() + "]" + " : " + msg;

		System.out.println(output);

		for(Channel channel : CHANNELS)
		{
			if (channel != incoming)
			{
				channel.write(output + "\n");
			}
		}
	}
}
