package net.jimboi.apricot.base.assets.resource;

import net.jimboi.boron.base_ab.asset.ResourceLoader;
import net.jimboi.boron.base_ab.asset.ResourceParameter;
import net.jimboi.boron.base_ab.sprite.SpriteUtil;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;
import net.jimboi.boron.base_ab.sprite.TextureAtlasData;

import org.qsilver.ResourceLocation;
import org.zilar.loader.TEXALoader;

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
