package canary.zilar.loader;

import org.lwjgl.BufferUtils;
import canary.qsilver.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.BufferUtils.createByteBuffer;

/**
 * Created by Andy on 4/30/17.
 */
public class BitmapLoader
{
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity)
	{
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public static ByteBuffer read(String filepath, int bufferSize)
	{
		ByteBuffer buffer = null;

		try
		{
			Path path = Paths.get(filepath);
			if (Files.isReadable(path))
			{
				try (SeekableByteChannel fc = Files.newByteChannel(path))
				{
					buffer = createByteBuffer((int) fc.size() + 1);
					while (fc.read(buffer) != -1) {}
				}
			}
			else
			{
				try (InputStream source = FileUtil.class.getClassLoader().getResourceAsStream(filepath);
				     ReadableByteChannel rbc = Channels.newChannel(source))
				{
					buffer = createByteBuffer(bufferSize);

					while (true)
					{
						int bytes = rbc.read(buffer);
						if (bytes == -1) break;
						if (buffer.remaining() == 0)
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				}
			}

			buffer.flip();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return buffer;
	}
}
