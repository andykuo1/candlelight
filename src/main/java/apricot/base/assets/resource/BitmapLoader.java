package apricot.base.assets.resource;

import apricot.base.asset.ResourceLoader;
import apricot.base.asset.ResourceParameter;

import apricot.bstone.mogli.Bitmap;
import apricot.qsilver.ResourceLocation;

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
