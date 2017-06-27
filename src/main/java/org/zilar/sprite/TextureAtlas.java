package org.zilar.sprite;

import org.bstone.mogli.Texture;
import org.zilar.asset.Asset;

/**
 * Created by Andy on 5/10/17.
 */
public abstract class TextureAtlas
{
	protected final Asset<Texture> texture;

	public TextureAtlas(Asset<Texture> texture)
	{
		this.texture = texture;
	}

	public Asset<Texture> getTexture()
	{
		return this.texture;
	}

	public SpriteSheet createSpriteSheet(int... indices)
	{
		Sprite[] sprites = new Sprite[indices.length];
		for(int i = 0; i < sprites.length; ++i)
		{
			sprites[i] = this.getSprite(indices[i]);
		}
		return new SpriteSheet(this, sprites);
	}

	public abstract Sprite getSprite(int index);

	public abstract int size();
}
