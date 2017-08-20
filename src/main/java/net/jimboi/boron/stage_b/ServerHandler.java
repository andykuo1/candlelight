package net.jimboi.boron.stage_b;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Andy on 8/14/17.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		LoopBackTimeStamp timestamp = (LoopBackTimeStamp) msg;
		timestamp.receiveTimeStamp = System.nanoTime();
		System.out.println(new Date().toString() + " - TIME: " + 1.0 * timestamp.getElapsedTimeInNanoseconds());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE)
			{
				ctx.writeAndFlush(new LoopBackTimeStamp());
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}
}
