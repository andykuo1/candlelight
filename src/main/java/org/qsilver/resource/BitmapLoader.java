package org.qsilver.resource;

import net.jimboi.boron.base_ab.asset.ResourceLoader;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

import org.bstone.mogli.Bitmap;
import org.zilar.resource.ResourceLocation;

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
