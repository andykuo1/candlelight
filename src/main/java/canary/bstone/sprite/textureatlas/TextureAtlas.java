package canary.bstone.sprite.textureatlas;

import canary.bstone.RefCountSet;
import canary.bstone.asset.Asset;
import canary.bstone.mogli.Texture;

import java.util.Set;

/**
 * Created by Andy on 10/31/17.
 */
public class TextureAtlas implements AutoCloseable
{
	public static final Set<TextureAtlas> TEXTUREATLAS = new RefCountSet<>("TextureAtlas");

	private final Asset<Texture> texture;
	private final SubTexture[] subtextures;

	public TextureAtlas(Asset<Texture> texture, int count)
	{
		this.texture = texture;
		this.subtextures = createSubTextures(this, count);

		TEXTUREATLAS.add(this);
	}

	@Override
	public void close() throws Exception
	{
		TEXTUREATLAS.remove(this);
	}

	public Asset<Texture> getTexture()
	{
		return this.texture;
	}

	public SubTexture getSubTexture(int index)
	{
		return this.subtextures[index];
	}

	public int length()
	{
		return this.subtextures.length;
	}

	private static SubTexture[] createSubTextures(TextureAtlas atlas, int count)
	{
		SubTexture[] result = new SubTexture[count];
		for(int i = 0; i < result.length; ++i)
		{
			result[i] = new SubTexture(atlas);
		}
		return result;
	}
}
