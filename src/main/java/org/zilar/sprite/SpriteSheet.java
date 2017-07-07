package org.zilar.sprite;

import org.qsilver.asset.Asset;

/**
 * Created by Andy on 5/11/17.
 *
 * Remember: as of right now, you must register this with the animation manager to actually run
 */
public final class SpriteSheet
{
	private Asset<TextureAtlas> textureAtlas;
	private final int[] indices;
	private int index = 0;

	public SpriteSheet(Asset<TextureAtlas> textureAtlas, int startIndex, int length)
	{
		this(textureAtlas, new int[length]);

		for(int i = 0; i < this.indices.length; ++i)
		{
			this.indices[i] = startIndex + i;
		}
	}

	public SpriteSheet(Asset<TextureAtlas> textureAtlas, int[] indices)
	{
		this.textureAtlas = textureAtlas;
		this.indices = indices;
	}

	public void next()
	{
		this.index++;

		if (this.index >= this.indices.length) this.index = 0;
	}

	public Sprite get()
	{
		return this.get(this.index);
	}

	public Sprite get(int index)
	{
		return this.textureAtlas.getSource().getSprite(this.indices[index]);
	}

	public void flip()
	{
		int len2 = this.indices.length / 2;
		for(int i = 0; i < len2; ++i)
		{
			int j = this.indices.length - 1 - i;
			int index = this.indices[i];
			this.indices[i] = this.indices[j];
			this.indices[j] = index;
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

	public int length()
	{
		return this.indices.length;
	}
}
