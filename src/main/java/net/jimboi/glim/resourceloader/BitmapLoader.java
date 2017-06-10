package net.jimboi.glim.resourceloader;

import net.jimboi.mod2.asset.ResourceLoader;
import net.jimboi.mod2.asset.ResourceParameter;
import net.jimboi.mod2.resource.ResourceLocation;

import org.bstone.mogli.Bitmap;

/**
 * Created by Andy on 6/10/17.
 */
public class BitmapLoader implements ResourceLoader<Bitmap, BitmapLoader.BitmapParameter>
{
	@Override
	public Bitmap load(BitmapParameter args)
	{
		return new Bitmap(args.location);
	}

	public static class BitmapParameter implements ResourceParameter<Bitmap>
	{
		public ResourceLocation location;

		public BitmapParameter(ResourceLocation location)
		{
			this.location = location;
		}
	}
}
