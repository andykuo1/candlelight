package org.zilar.loader;

import net.jimboi.boron.base_ab.sprite.TextureAtlas;

import org.qsilver.ResourceLocation;

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
