package org.zilar.loader;

import org.qsilver.ResourceLocation;
import org.zilar.sprite.TextureAtlas;

/**
 * Created by Andy on 7/6/17.
 */
public class TEXALoader
{
	public static TextureAtlas read(ResourceLocation location)
	{
		return read(location.getFilePath());
	}

	public static TextureAtlas read(String filepath)
	{
		//TODO: this is not yet implemented
		throw new UnsupportedOperationException("Not yet implemented!");
	}
}
