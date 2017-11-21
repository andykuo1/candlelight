package net.jimboi.test.multiuser;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by Andy on 8/14/17.
 */
public class TimeStampDecoder extends ByteToMessageDecoder
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		final int messageLength = Long.SIZE / Byte.SIZE * 2;

		if (in.readableBytes() < messageLength)
		{
			return;
		}

		byte[] data = new byte[messageLength];
		in.readBytes(data, 0, messageLength);
		LoopBackTimeStamp loopBackTimeStamp = new LoopBackTimeStamp();
		loopBackTimeStamp.fromByteArray(data);
		out.add(loopBackTimeStamp);
	}
}
