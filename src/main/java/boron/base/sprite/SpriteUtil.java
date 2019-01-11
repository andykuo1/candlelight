package boron.base.sprite;

/**
 * Created by Andy on 7/6/17.
 */
public class SpriteUtil
{
	public static TextureAtlas createTextureAtlas(TextureAtlasData data)
	{
		return new TextureAtlas(data.sprites);
	}
}
