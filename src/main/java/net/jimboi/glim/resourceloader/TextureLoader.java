package net.jimboi.glim.resourceloader;

import net.jimboi.mod2.asset.Asset;
import net.jimboi.mod2.asset.ResourceLoader;
import net.jimboi.mod2.asset.ResourceParameter;

import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;

/**
 * Created by Andy on 6/10/17.
 */
public class TextureLoader implements ResourceLoader<Texture, TextureLoader.TextureParameter>
{
	@Override
	public Texture load(TextureParameter args)
	{
		return new Texture(args.bitmap.getSource(), args.minMagFilter, args.wrapMode);
	}

	public static class TextureParameter implements ResourceParameter<Texture>
	{
		public Asset<Bitmap> bitmap;
		public int minMagFilter;
		public int wrapMode;

		public TextureParameter(Asset<Bitmap> bitmap, int minMagFilter, int wrapMode)
		{
			this.bitmap = bitmap;
			this.minMagFilter = minMagFilter;
			this.wrapMode = wrapMode;
		}
	}
}
