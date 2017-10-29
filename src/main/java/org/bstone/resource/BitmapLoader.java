package org.bstone.resource;

import org.bstone.mogli.Bitmap;
import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Andy on 10/26/17.
 */
public class BitmapLoader implements ResourceLoader<Bitmap>
{
	@Override
	public Bitmap load(InputStream stream) throws Exception
	{
		ByteBuffer buffer = BufferUtils.createByteBuffer(256);
		try (ReadableByteChannel channel = Channels.newChannel(stream))
		{
			while (channel.read(buffer) != -1)
			{
				if (buffer.remaining() == 0)
				{
					buffer = resizeBuffer(buffer, buffer.capacity() * 2);
				}
			}
		}
		buffer.flip();

		return new Bitmap(buffer);
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity)
	{
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}
}
