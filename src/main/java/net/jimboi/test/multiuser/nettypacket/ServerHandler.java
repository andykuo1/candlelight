package net.jimboi.test.multiuser.nettypacket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Andy on 8/15/17.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter
{
	protected final Server server;

	public ServerHandler(Server server)
	{
		this.server = server;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[SERVER] : " + incoming.remoteAddress() + " has joined!";

		System.out.println(output);

		for(Channel channel : this.server.getChannels())
		{
			channel.writeAndFlush(output + "\n");
		}

		this.server.getChannels().add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[SERVER] : " + incoming.remoteAddress() + " has left!";

		System.out.println(output);

		for(Channel channel : this.server.getChannels())
		{
			channel.write(output + "\n");
		}

		this.server.getChannels().remove(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		Channel incoming = ctx.channel();
		String output = "[" + incoming.remoteAddress() + "]" + " : " + msg;

		System.out.println(output);

		for(Channel channel : this.server.getChannels())
		{
			if (channel != incoming)
			{
				channel.write(output + "\n");
			}
		}
	}
}
