package org.bstone.network.pipeline;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

/**
 * Created by Andy on 11/30/17.
 */
public class VarInt21FrameDecoder extends ByteToMessageDecoder
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		in.markReaderIndex();

		byte[] b = new byte[3];

		for(int i = 0; i < b.length; ++i)
		{
			if (!in.isReadable())
			{
				in.resetReaderIndex();
				return;
			}

			b[i] = in.readByte();

			if (b[i] >= 0)
			{
				try
				{
					int j = readVarInt21(in);
					if (in.readableBytes() >= j)
					{
						out.add(in.readBytes(j));
						return;
					}

					in.resetReaderIndex();
				}
				finally
				{
					in.release();
				}

				return;
			}
		}

		throw new CorruptedFrameException("length is greater than 21-bit varint");
	}

	/**
	 * Reads a compressed int from the buffer. It will read 5 byte-sized chunks
	 * whose most significant bit signifies whether another byte should be read.
	 * In other words, it reads a 21-bit varint.
	 */
	public static int readVarInt21(ByteBuf src)
	{
		int i = 0;
		int j = 0;

		while (true)
		{
			byte b0 = src.readByte();
			i |= (b0 & 127) << j++ * 7;

			if (j > 5)
			{
				throw new IllegalArgumentException("varint bytes must be less than 5");
			}

			if ((b0 & 128) != 128)
			{
				break;
			}
		}

		return i;
	}

}
