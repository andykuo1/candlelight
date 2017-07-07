package org.zilar.sprite;

/**
 * Created by Andy on 5/10/17.
 */
public final class TextureAtlas implements AutoCloseable
{
	private final Sprite[] sprites;

	public TextureAtlas(Sprite... sprites)
	{
		this.sprites = sprites;
	}

	@Override
	public void close() throws Exception
	{
		//Nothing to dispose of yet...
	}

	public Sprite getSprite(int index)
	{
		return this.sprites[index];
	}

	public int size()
	{
		return this.sprites.length;
	}
}
