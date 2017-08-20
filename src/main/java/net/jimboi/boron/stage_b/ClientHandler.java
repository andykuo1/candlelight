package net.jimboi.boron.stage_b;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Andy on 8/14/17.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		LoopBackTimeStamp timestamp = (LoopBackTimeStamp) msg;
		ctx.writeAndFlush(timestamp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}
}
