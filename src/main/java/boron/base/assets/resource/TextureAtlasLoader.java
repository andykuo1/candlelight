package boron.base.assets.resource;

import boron.base.asset.ResourceLoader;
import boron.base.asset.ResourceParameter;
import boron.base.sprite.SpriteUtil;
import boron.base.sprite.TextureAtlas;
import boron.base.sprite.TextureAtlasData;

import boron.qsilver.ResourceLocation;
import boron.zilar.loader.TEXALoader;

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
