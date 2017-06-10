package org.bstone.mogli;

import org.bstone.RefCountSet;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;

import java.nio.ByteBuffer;
import java.util.Set;

/**
 * Created by Andy on 4/6/17.
 */
public final class Texture implements AutoCloseable
{
	public static final Set<Texture> TEXTURES = new RefCountSet<>();

	private static int getInternalTextureFormat(Bitmap.Format format)
	{
		switch (format)
		{
			case GRAYSCALE:
				return GL11.GL_LUMINANCE;
			case GRAYSCALEALPHA:
				return GL11.GL_LUMINANCE_ALPHA;
			case RGB: return GL21.GL_SRGB;
			case RGBA: return GL21.GL_SRGB_ALPHA;
			case DEPTH:
				return GL14.GL_DEPTH_COMPONENT16;
			default:
				throw new UnsupportedOperationException("Unrecognized bitmap format!");
		}
	}

	private static int getTextureFormat(Bitmap.Format format)
	{
		switch (format)
		{
			case GRAYSCALE:
				return GL11.GL_LUMINANCE;
			case GRAYSCALEALPHA:
				return GL11.GL_LUMINANCE_ALPHA;
			case RGB: return GL11.GL_RGB;
			case RGBA: return GL11.GL_RGBA;
			case DEPTH:
				return GL11.GL_DEPTH_COMPONENT;
			default:
				throw new UnsupportedOperationException("Unrecognized bitmap format!");
		}
	}

	private static int getTextureBufferType(Bitmap.Format format)
	{
		switch (format)
		{
			case GRAYSCALE:
			case GRAYSCALEALPHA:
			case RGB:
			case RGBA:
				return GL11.GL_UNSIGNED_BYTE;

			case DEPTH:
				return GL11.GL_FLOAT;

			default:
				throw new UnsupportedOperationException("Unrecognized bitmap format!");
		}
	}

	private final Bitmap bitmap;

	private final int width;
	private final int height;

	private int handle;

	public Texture(int width, int height, int minMagFilter, int wrapMode, Bitmap.Format pixelFormat)
	{
		this.bitmap = null;

		this.width = width;
		this.height = height;

		this.handle = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.handle);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minMagFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, minMagFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapMode);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
				0,
				getInternalTextureFormat(pixelFormat),
				this.width,
				this.height,
				0,
				getTextureFormat(pixelFormat),
				getTextureBufferType(pixelFormat),
				(ByteBuffer) null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		TEXTURES.add(this);
	}

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
		Bitmap.Format format = this.bitmap.getFormat();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
				0,
				getInternalTextureFormat(format),
				this.bitmap.getWidth(),
				this.bitmap.getHeight(),
				0,
				getTextureFormat(format),
				getTextureBufferType(format),
				this.bitmap.getPixelBuffer());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		TEXTURES.add(this);
	}

	@Override
	public void close()
	{
		GL11.glDeleteTextures(this.handle);

		TEXTURES.remove(this);
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
