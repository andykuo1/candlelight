package net.jimboi.stage_b.glim.resourceloader;

import net.jimboi.stage_b.gnome.asset.ResourceLoader;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

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
