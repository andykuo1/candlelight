package net.jimboi.test.chubbycat.message;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * Created by Andy on 11/29/17.
 */
public class MessageEncoder extends MessageToMessageEncoder<Message>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception
	{
		String s = msg.getOwner() + "\t" + msg.getData() + "\n";
		out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(s), Charset.defaultCharset()));
	}
}
