package org.bstone.mogli;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;

/**
 * Created by Andy on 4/6/17.
 */
public final class Texture implements AutoCloseable
{
	private static int getInternalTextureFormat(Bitmap.Format format, boolean srgb)
	{
		switch (format)
		{
			case Grayscale: return GL11.GL_LUMINANCE;
			case GrayscaleAlpha: return GL11.GL_LUMINANCE_ALPHA;
			case RGB: return GL21.GL_SRGB;
			case RGBA: return GL21.GL_SRGB_ALPHA;
			default:
				throw new UnsupportedOperationException("Unrecognized bitmap format!");
		}
	}

	private static int getTextureFormat(Bitmap.Format format)
	{
		switch (format)
		{
			case Grayscale: return GL11.GL_LUMINANCE;
			case GrayscaleAlpha: return GL11.GL_LUMINANCE_ALPHA;
			case RGB: return GL11.GL_RGB;
			case RGBA: return GL11.GL_RGBA;
			default:
				throw new UnsupportedOperationException("Unrecognized bitmap format!");
		}
	}

	private final Bitmap bitmap;

	private final float width;
	private final float height;

	private int handle;

	public Texture(Bitmap bitmap, int minMagFilter, int wrapMode)
	{
		this.bitmap = bitmap;

		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();

		this.handle = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.handle);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minMagFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, minMagFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapMode);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
				0,
				getInternalTextureFormat(this.bitmap.getFormat(), true),
				this.bitmap.getWidth(),
				this.bitmap.getHeight(),
				0,
				getTextureFormat(this.bitmap.getFormat()),
				GL11.GL_UNSIGNED_BYTE,
				this.bitmap.getPixelBuffer());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@Override
	public void close()
	{
		GL11.glDeleteTextures(this.handle);
	}

	public int handle()
	{
		return this.handle;
	}

	public float width()
	{
		return this.width;
	}

	public float height()
	{
		return this.height;
	}

	public void bind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.handle);
	}

	public void unbind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public Bitmap bitmap()
	{
		return this.bitmap;
	}
}
