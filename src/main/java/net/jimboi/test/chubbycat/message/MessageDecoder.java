package net.jimboi.test.chubbycat.message;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Created by Andy on 11/29/17.
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf>
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
	{
		String owner;
		String data;

		String s = msg.toString(Charset.defaultCharset());
		int i = s.indexOf('\t');
		if (i != -1)
		{
			owner = s.substring(0, i);
			data = s.substring(i + 1);
		}
		else
		{
			owner = "";
			data = s;
		}

		out.add(new Message(owner, data));
	}
}
