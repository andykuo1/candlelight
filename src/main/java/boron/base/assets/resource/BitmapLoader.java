package boron.base.assets.resource;

import boron.base.asset.ResourceLoader;
import boron.base.asset.ResourceParameter;

import boron.bstone.mogli.Bitmap;
import boron.qsilver.ResourceLocation;

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
