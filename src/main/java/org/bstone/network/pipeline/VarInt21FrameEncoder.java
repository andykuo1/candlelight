package org.bstone.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Andy on 11/30/17.
 */
public class VarInt21FrameEncoder extends MessageToByteEncoder<ByteBuf>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception
	{
		int i = msg.readableBytes();
		int j = getVarIntSize(i);

		if (j > 3)
		{
			throw new IllegalArgumentException("unable to compress varint of " + i + " bytes into maximum 3 bytes");
		}
		else
		{
			out.ensureWritable(j + i);
			writeVarInt(i, out);
			out.writeBytes(msg, msg.readerIndex(), i);
		}
	}

	/**
	 * Calculates the number of bytes required to compress the int (0-5) into a
	 * VarInt.
	 */
	public static int getVarIntSize(int value)
	{
		for (int i = 1; i < 5; ++i)
		{
			if ((value & -1 << i * 7) == 0)
			{
				return i;
			}
		}

		return 5;
	}

	/**
	 * Writes a compressed int to the buffer. The smallest number of bytes to
	 * fit the passed int will be written. Of each such byte only 7 bits will
	 * be used to describe the actual value since its most significant bit
	 * dictate whether the next byte is part of that same int.
	 */
	public static void writeVarInt(int value, ByteBuf dst)
	{
		while ((value & -128) != 0)
		{
			dst.writeByte(value & 127 | 128);
			value >>>= 7;
		}

		dst.writeByte(value);
	}
}
