package org.bstone.mogli;

import org.bstone.RefCountSet;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.qsilver.loader.BitmapLoader;
import org.zilar.resource.ResourceLocation;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Set;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * Created by Andy on 4/6/17.
 */
public final class Bitmap implements AutoCloseable
{
	public static final Set<Bitmap> BITMAPS = new RefCountSet<>("Bitmap");

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
	private final boolean loadFromSTB;

	public Bitmap(ResourceLocation location)
	{
		this(BitmapLoader.read(location.getFilePath(), 8 * 1024));
	}

	public Bitmap(ByteBuffer imageBuffer)
	{
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		if (!stbi_info_from_memory(imageBuffer, w, h, comp))
			throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

		this.width = w.get(0);
		this.height = h.get(0);
		this.format = Format.valueOf(comp.get(0));

		this.pixels = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		this.loadFromSTB = true;
		if (this.pixels == null)
			throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

		//TODO: This is because OpenGL texture origin is bottom-left, NOT top-left
		this.flipVertically();
		this.flipHorizontally();

		BITMAPS.add(this);
	}

	public Bitmap(ByteBuffer pixelBuffer, int width, int height, Format format)
	{
		this.width = width;
		this.height = height;
		this.format = format;
		this.pixels = pixelBuffer == null ? BufferUtils.createByteBuffer(this.width * this.height * this.format.getChannel()) : pixelBuffer;
		this.loadFromSTB = false;

		BITMAPS.add(this);
	}

	@Override
	public void close()
	{
		if (this.loadFromSTB)
		{
			STBImage.stbi_image_free(this.pixels);
		}

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

	public void flipHorizontally()
	{
		int colSize = this.height;
		byte[] colBuffer = new byte[colSize];
		int halfCols = this.width / 2;

		for(int coly = 0; coly < halfCols; ++coly)
		{
			int col = getPixelOffset(coly, 0, this.width, this.height, this.format);
			int opposite = getPixelOffset(this.width - coly - 1, 0, this.width, this.height, this.format);

			for(int i = 0; i < colSize; ++i)
			{
				int k = i * this.width * this.format.channel;
				for(int j = 0; j < this.format.channel; ++j)
				{
					int p = col + k + j;
					int q = opposite + k + j;
					colBuffer[i] = this.pixels.get(p);
					this.pixels.put(p, this.pixels.get(q));
					this.pixels.put(q, colBuffer[i]);
				}
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
