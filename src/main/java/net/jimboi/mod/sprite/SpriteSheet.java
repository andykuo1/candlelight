package net.jimboi.mod.sprite;

/**
 * Created by Andy on 5/11/17.
 */
public class SpriteSheet
{
	private final TextureAtlas atlas;
	private final Sprite[] sprites;
	private int index = 0;

	protected SpriteSheet(TextureAtlas atlas, Sprite... sprites)
	{
		this.atlas = atlas;
		this.sprites = sprites;
	}

	public Sprite get()
	{
		if (this.index >= this.sprites.length) this.index = 0;
		return this.sprites[this.index++];
	}

	public Sprite get(int index)
	{
		return this.sprites[index];
	}

	public void flip()
	{
		int len2 = this.sprites.length / 2;
		for(int i = 0; i < len2; ++i)
		{
			int j = this.sprites.length - 1 - i;
			Sprite sprite = this.sprites[i];
			this.sprites[i] = this.sprites[j];
			this.sprites[j] = sprite;
		}
	}

	public void reset()
	{
		this.index = 0;
	}

	public void setSpriteIndex(int index)
	{
		this.index = index;
	}

	public int getSpriteIndex()
	{
		return this.index;
	}

	public TextureAtlas getTextureAtlas()
	{
		return this.atlas;
	}

	public int length()
	{
		return this.sprites.length;
	}
}
