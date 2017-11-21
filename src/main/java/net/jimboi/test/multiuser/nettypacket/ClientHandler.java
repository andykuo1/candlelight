package net.jimboi.test.multiuser.nettypacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Andy on 8/15/17.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter
{
	protected final Client client;

	public ClientHandler(Client client)
	{
		this.client = client;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		String str = (String) msg;
		int i = str.indexOf(':');
		if (i != -1)
		{

		}
	}
}