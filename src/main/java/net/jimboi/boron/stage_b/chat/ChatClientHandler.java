package net.jimboi.boron.stage_b.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Andy on 8/14/17.
 */
public class ChatClientHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		String str = (String) msg;
		System.out.println(msg);
	}
}
