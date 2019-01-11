package canary.bstone.sprite;

import canary.bstone.asset.Asset;
import canary.bstone.mogli.Texture;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import org.joml.Vector2f;

/**
 * Created by Andy on 10/31/17.
 */
public class SubTextureSprite extends Sprite
{
	protected final Asset<TextureAtlas> atlas;
	protected final int atlasIndex;

	public SubTextureSprite(Asset<TextureAtlas> atlas, int subindex)
	{
		this.atlas = atlas;
		this.atlasIndex = subindex;
	}

	@Override
	public Vector2f getUV(Vector2f dst)
	{
		SubTexture subtexture = this.atlas.get().getSubTexture(this.atlasIndex);
		dst.set(subtexture.getU(), subtexture.getV());
		return dst;
	}

	@Override
	public Vector2f getSize(Vector2f dst)
	{
		SubTexture subtexture = this.atlas.get().getSubTexture(this.atlasIndex);
		dst.set(subtexture.getWidth(), subtexture.getHeight());
		return dst;
	}

	public int getSubTextureIndex()
	{
		return this.atlasIndex;
	}

	public Asset<TextureAtlas> getTextureAtlas()
	{
		return this.atlas;
	}

	@Override
	public Asset<Texture> getTexture()
	{
		return this.atlas.get().getTexture();
	}
}
