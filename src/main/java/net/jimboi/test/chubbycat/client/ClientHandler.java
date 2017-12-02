package net.jimboi.test.chubbycat.client;

import net.jimboi.test.chubbycat.ChubbyCat;
import net.jimboi.test.chubbycat.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Andy on 11/29/17.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message>
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception
	{
		ChubbyCat.CONSOLE.println(msg.toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}
}
