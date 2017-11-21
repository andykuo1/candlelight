package net.jimboi.test.multiuser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Andy on 8/14/17.
 */
public class TimeStampEncoder extends MessageToByteEncoder<LoopBackTimeStamp>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, LoopBackTimeStamp msg, ByteBuf out) throws Exception
	{
		out.writeBytes(msg.toByteArray());
	}
}
