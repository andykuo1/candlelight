package org.zilar.sprite;

import org.bstone.mogli.Texture;
import org.qsilver.asset.Asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 7/6/17.
 */
public class TextureAtlasBuilder
{
	private final List<Sprite> sprites = new ArrayList<>();

	private Asset<Texture> texture;
	private int width;
	private int height;

	public TextureAtlasBuilder()
	{
	}

	public TextureAtlasBuilder add(Asset<Texture> texture, int spriteX, int spriteY, int spriteWidth, int spriteHeight)
	{
		if (this.texture != texture)
		{
			this.texture = texture;
			this.width = this.texture.getSource().width();
			this.height = this.texture.getSource().height();
		}

		float u = (this.width - spriteX - spriteWidth) / (float) this.width;
		float v = (this.height - spriteY - spriteHeight) / (float) this.height;
		float w = spriteWidth / (float) this.width;
		float h = spriteHeight / (float) this.height;

		this.sprites.add(new Sprite(this.texture, u, v, w, h));

		return this;
	}

	public TextureAtlasBuilder addHorizontalStrip(Asset<Texture> texture, int spriteX, int spriteY, int spriteWidth, int spriteHeight, int borderMargin, int spriteCount)
	{
		for (int i = 0; i < spriteCount; ++i)
		{
			this.add(texture, spriteX + (i * (spriteWidth + borderMargin)), spriteY, spriteWidth, spriteHeight);
		}

		return this;
	}

	public TextureAtlasBuilder addVerticalStrip(Asset<Texture> texture, int spriteX, int spriteY, int spriteWidth, int spriteHeight, int borderMargin, int spriteCount)
	{
		for (int i = 0; i < spriteCount; ++i)
		{
			this.add(texture, spriteX, spriteY + (i * (spriteHeight + borderMargin)), spriteWidth, spriteHeight);
		}

		return this;
	}

	public TextureAtlasBuilder addTileSheet(Asset<Texture> texture, int spriteX, int spriteY, int spriteWidth, int spriteHeight, int borderMarginX, int borderMarginY, int spriteRowLength, int spriteColumnLength)
	{
		for(int i = 0; i < spriteColumnLength; ++i)
		{
			this.addHorizontalStrip(texture, spriteX, spriteY + (i * (spriteHeight + borderMarginY)), spriteWidth, spriteHeight, borderMarginX, spriteRowLength);
		}

		return this;
	}

	public TextureAtlasBuilder addNineSheet(Asset<Texture> texture, int offsetX, int offsetY, int leftWidth, int bodyWidth, int rightWidth, int topHeight, int bodyHeight, int bottomHeight)
	{
		int x = offsetX;
		int y = offsetY;
		//Top
		this.add(texture, x, y, leftWidth, topHeight);
		this.add(texture, x + leftWidth, y, bodyWidth, topHeight);
		this.add(texture, x + leftWidth + bodyWidth, y, rightWidth, topHeight);

		//Middle
		this.add(texture, x, y + topHeight, leftWidth, bodyHeight);
		this.add(texture, x + leftWidth, y + topHeight, bodyWidth, bodyHeight);
		this.add(texture, x + leftWidth + bodyWidth, y + topHeight, rightWidth, bodyHeight);

		//Bottom
		this.add(texture, x, y + topHeight + bodyHeight, leftWidth, bottomHeight);
		this.add(texture, x + leftWidth, y + topHeight + bodyHeight, bodyWidth, bottomHeight);
		this.add(texture, x + leftWidth + bodyWidth, y + topHeight + bodyHeight, rightWidth, bottomHeight);

		return this;
	}

	public TextureAtlasData bake()
	{
		Sprite[] sprites = new Sprite[this.sprites.size()];
		int i = 0;
		for(Sprite sprite : this.sprites)
		{
			if (sprite == null) continue;
			if (i >= sprites.length) break;

			sprites[i++] = sprite;
		}
		return new TextureAtlasData(sprites);
	}

	public void clear()
	{
		this.sprites.clear();

		this.texture = null;
		this.width = 0;
		this.height = 0;
	}
}
