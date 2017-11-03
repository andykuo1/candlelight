package org.zilar.resource;

import net.jimboi.boron.base_ab.asset.ResourceLoader;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

import org.qsilver.ResourceLocation;
import org.zilar.loader.TEXALoader;
import org.zilar.sprite.SpriteUtil;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasData;

/**
 * Created by Andy on 7/6/17.
 */
public class TextureAtlasLoader implements ResourceLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameter>
{
	@Override
	public TextureAtlas load(TextureAtlasParameter args)
	{
		if (args.data != null)
		{
			return SpriteUtil.createTextureAtlas(args.data);
		}

		if (args.location != null)
		{
			return TEXALoader.read(args.location);
		}

		return new TextureAtlas();
	}

	public static class TextureAtlasParameter implements ResourceParameter<TextureAtlas>
	{
		public ResourceLocation location;
		public TextureAtlasData data;

		public TextureAtlasParameter(ResourceLocation location)
		{
			this.location = location;
		}

		public TextureAtlasParameter(TextureAtlasData data)
		{
			this.data = data;
		}
	}
}
