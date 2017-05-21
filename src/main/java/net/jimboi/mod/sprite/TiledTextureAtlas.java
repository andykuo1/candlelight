package net.jimboi.mod.sprite;

import org.bstone.mogli.Texture;

/**
 * Created by Andy on 5/10/17.
 */
public class TiledTextureAtlas extends TextureAtlas
{
	private Sprite[] sprites;

	private final int spriteWidth;
	private final int spriteHeight;

	private final int rowSize;
	private final int colSize;

	public TiledTextureAtlas(Texture texture, int spriteWidth, int spriteHeight)
	{
		super(texture);

		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		this.rowSize = ((int) this.texture.width()) / (this.spriteWidth);
		this.colSize = ((int) this.texture.height()) / (this.spriteHeight);

		this.sprites = new Sprite[this.rowSize * this.colSize];
	}

	public Sprite getSprite(int x, int y)
	{
		int i = x + y * this.rowSize;
		Sprite sprite = this.sprites[i];
		if (sprite == null)
		{
			sprite = this.sprites[i] = new Sprite(this.texture, x * this.spriteWidth, y * this.spriteHeight, this.spriteWidth, this.spriteHeight);
		}
		return sprite;
	}

	@Override
	public Sprite getSprite(int index)
	{
		Sprite sprite = this.sprites[index];
		if (sprite == null)
		{
			sprite = this.sprites[index] = new Sprite(this.texture, (index % this.rowSize) * this.spriteWidth, (index / this.rowSize) * this.spriteHeight, this.spriteWidth, this.spriteHeight);
		}
		return sprite;
	}

	public int getSpriteIndexOf(int x, int y)
	{
		return x + y * this.rowSize;
	}

	public int getSpriteWidth()
	{
		return this.spriteWidth;
	}

	public int getSpriteHeight()
	{
		return this.spriteHeight;
	}

	public int getRowSize()
	{
		return this.rowSize;
	}

	public int getColumnSize()
	{
		return this.colSize;
	}

	@Override
	public int size()
	{
		return this.sprites.length;
	}
}
