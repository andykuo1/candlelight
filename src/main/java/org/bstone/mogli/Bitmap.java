package org.bstone.mogli;

import net.jimboi.mod2.resource.ResourceLocation;

import org.bstone.loader.BitmapLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * Created by Andy on 4/6/17.
 */
public final class Bitmap implements AutoCloseable
{
	public static final Set<Bitmap> BITMAPS = new HashSet<>();

	public enum Format
	{
		GRAYSCALE(1),
		GRAYSCALEALPHA(2),
		RGB(3),
		RGBA(4),
		DEPTH(1);

		private final int channel;

		Format(int channel)
		{
			this.channel = channel;
		}

		public int getChannel()
		{
			return this.channel;
		}

		public static Format valueOf(int channel)
		{
			return Format.values()[channel - 1];
		}
	}

	private final int width;
	private final int height;
	private final Format format;
	private final ByteBuffer pixels;

	public Bitmap(ResourceLocation location)
	{
		ByteBuffer imageBuffer = BitmapLoader.read(location.getFilePath(), 8 * 1024);

		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		if (!stbi_info_from_memory(imageBuffer, w, h, comp))
			throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

		this.width = w.get(0);
		this.height = h.get(0);
		this.format = Format.valueOf(comp.get(0));

		this.pixels = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		if (this.pixels == null)
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

		BITMAPS.add(this);
	}

	@Override
	public void close()
	{
		STBImage.stbi_image_free(this.pixels);

		BITMAPS.remove(this);
	}

	public void flipVertically()
	{
		int rowSize = this.format.getChannel() * this.width;
		byte[] rowBuffer = new byte[rowSize];
		int halfRows = this.height / 2;

		for(int rowx = 0; rowx < halfRows; ++rowx)
		{
			int row = getPixelOffset(0, rowx, this.width, this.height, this.format);
			int oppositeRow = getPixelOffset(0, this.height - rowx - 1, this.width, this.height, this.format);

			for(int i = 0; i < rowSize; ++i)
			{
				rowBuffer[i] = this.pixels.get(row + i);
				this.pixels.put(row + i, this.pixels.get(oppositeRow + i));
				this.pixels.put(oppositeRow + i, rowBuffer[i]);
			}
		}
	}

	private int getPixelOffset(int column, int row, int width, int height, Format format)
	{
		return (row * width + column) * format.getChannel();
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public Format getFormat()
	{
		return this.format;
	}

	public ByteBuffer getPixelBuffer()
	{
		return this.pixels;
	}
}
