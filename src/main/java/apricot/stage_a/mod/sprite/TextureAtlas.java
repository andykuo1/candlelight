package apricot.stage_a.mod.sprite;

import apricot.bstone.mogli.Texture;

/**
 * Created by Andy on 5/10/17.
 */
public abstract class TextureAtlas
{
	protected final Texture texture;

	public TextureAtlas(Texture texture)
	{
		this.texture = texture;
	}

	public Texture getTexture()
	{
		return this.texture;
	}

	public SpriteSheet createSpriteSheet(int... indices)
	{
		Sprite[] sprites = new Sprite[indices.length];
		for (int i = 0; i < sprites.length; ++i)
		{
			sprites[i] = this.getSprite(indices[i]);
		}
		return new SpriteSheet(this, sprites);
	}

	public abstract Sprite getSprite(int index);

	public abstract int size();
}
