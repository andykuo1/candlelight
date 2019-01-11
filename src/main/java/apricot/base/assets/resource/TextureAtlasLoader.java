package apricot.base.assets.resource;

import apricot.base.asset.ResourceLoader;
import apricot.base.asset.ResourceParameter;
import apricot.base.sprite.SpriteUtil;
import apricot.base.sprite.TextureAtlas;
import apricot.base.sprite.TextureAtlasData;

import apricot.qsilver.ResourceLocation;
import apricot.zilar.loader.TEXALoader;

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
