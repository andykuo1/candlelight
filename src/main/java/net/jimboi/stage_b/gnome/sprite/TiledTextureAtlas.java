package net.jimboi.stage_b.gnome.sprite;

import net.jimboi.stage_b.gnome.asset.Asset;

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

	public TiledTextureAtlas(Asset<Texture> texture, int spriteWidth, int spriteHeight)
	{
		super(texture);

		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		Texture t = this.texture.getSource();
		this.rowSize = ((int) t.width()) / (this.spriteWidth);
		this.colSize = ((int) t.height()) / (this.spriteHeight);

		this.sprites = new Sprite[this.rowSize * this.colSize];
	}

	public Sprite getSprite(int x, int y)
	{
		return getSprite(x + y * this.rowSize);
	}

	@Override
	public Sprite getSprite(int index)
	{
		Sprite sprite = this.sprites[index];
		if (sprite == null)
		{
			float u = (index % this.rowSize) * this.spriteWidth;
			float v = (index / this.rowSize) * this.spriteHeight;
			float w = this.spriteWidth;
			float h = this.spriteHeight;
			Texture t = this.texture.getSource();
			float tw = t.width();
			float th = t.height();

			sprite = this.sprites[index] = new Sprite(this.texture, u / tw, v / th, w / tw, h / th);
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
