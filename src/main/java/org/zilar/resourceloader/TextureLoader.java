package org.zilar.resourceloader;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.qsilver.asset.Asset;
import org.qsilver.asset.ResourceLoader;
import org.qsilver.asset.ResourceParameter;

/**
 * Created by Andy on 6/10/17.
 */
public class TextureLoader implements ResourceLoader<Texture, TextureLoader.TextureParameter>
{
	@Override
	public Texture load(TextureParameter args)
	{
		if (args.bitmap != null)
		{
			return new Texture(args.bitmap.getSource(), args.minMagFilter, args.wrapMode);
		}
		else
		{
			return new Texture(args.width, args.height, args.minMagFilter, args.wrapMode, args.pixelFormat);
		}
	}

	public static class TextureParameter implements ResourceParameter<Texture>
	{
		public Asset<Bitmap> bitmap;

		public int minMagFilter;
		public int wrapMode;

		public int width;
		public int height;
		public Bitmap.Format pixelFormat;

		public TextureParameter(int width, int height, int minMagFilter, int wrapMode, Bitmap.Format pixelFormat)
		{
			this.bitmap = null;
			this.width = width;
			this.height = height;
			this.minMagFilter = minMagFilter;
			this.wrapMode = wrapMode;
			this.pixelFormat = pixelFormat;
		}

		public TextureParameter(Asset<Bitmap> bitmap, int minMagFilter, int wrapMode)
		{
			this.bitmap = bitmap;
			this.minMagFilter = minMagFilter;
			this.wrapMode = wrapMode;
		}
	}
}