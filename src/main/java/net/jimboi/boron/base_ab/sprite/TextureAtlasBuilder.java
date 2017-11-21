package net.jimboi.boron.base_ab.sprite;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 8/6/17.
 */
public class TextureAtlasBuilder
{
	private final List<Sprite> sprites = new ArrayList<>();

	private Asset<Texture> texture;
	private int width;
	private int height;

	public TextureAtlasBuilder(Asset<Texture> texture, int textureWidth, int textureHeight)
	{
		this.texture = texture;

		this.width = textureWidth;
		this.height = textureHeight;
	}

	public TextureAtlasBuilder add(int spriteX, int spriteY, int spriteWidth, int spriteHeight)
	{
		float u = (this.width - spriteX - spriteWidth) / (float) this.width;
		float v = (this.height - spriteY - spriteHeight) / (float) this.height;
		float w = spriteWidth / (float) this.width;
		float h = spriteHeight / (float) this.height;

		this.sprites.add(new Sprite(this.texture, u, v, w, h));

		return this;
	}

	public TextureAtlasBuilder addHorizontalStrip(int offsetX, int offsetY, int spriteWidth, int spriteHeight, int spacingX, int spriteCount)
	{
		for (int i = 0; i < spriteCount; ++i)
		{
			this.add(offsetX + (i * (spriteWidth + spacingX)), offsetY, spriteWidth, spriteHeight);
		}

		return this;
	}

	public TextureAtlasBuilder addVerticalStrip(int offsetX, int offsetY, int spriteWidth, int spriteHeight, int spacingY, int spriteCount)
	{
		for (int i = 0; i < spriteCount; ++i)
		{
			this.add(offsetX, offsetY + (i * (spriteHeight + spacingY)), spriteWidth, spriteHeight);
		}

		return this;
	}

	public TextureAtlasBuilder addTileSheet(int offsetX, int offsetY, int spriteWidth, int spriteHeight, int spacingX, int spacingY, int spriteRowLength, int spriteColumnLength)
	{
		for(int i = 0; i < spriteColumnLength; ++i)
		{
			this.addHorizontalStrip(offsetX, offsetY + (i * (spriteHeight + spacingY)), spriteWidth, spriteHeight, spacingX, spriteRowLength);
		}

		return this;
	}

	public TextureAtlasBuilder addNineSheet(int leftBorderSize, int rightBorderSize, int topBorderSize, int bottomBorderSize)
	{
		int bodyWidth = this.width - leftBorderSize - rightBorderSize;
		int bodyHeight = this.height - topBorderSize - bottomBorderSize;

		//TOP
		this.add(0, 0, leftBorderSize, topBorderSize);
		this.add(leftBorderSize, 0, bodyWidth, topBorderSize);
		this.add(leftBorderSize + bodyWidth, 0, rightBorderSize, topBorderSize);

		//BODY
		this.add(0, topBorderSize, leftBorderSize, bodyHeight);
		this.add(leftBorderSize, topBorderSize, bodyWidth, bodyHeight);
		this.add(leftBorderSize + bodyWidth, topBorderSize, rightBorderSize, bodyHeight);

		//BOTTOM
		this.add(0, topBorderSize + bodyHeight, leftBorderSize, bottomBorderSize);
		this.add(leftBorderSize, topBorderSize + bodyHeight, bodyWidth, bottomBorderSize);
		this.add(leftBorderSize + bodyWidth, topBorderSize + bodyHeight, rightBorderSize, bottomBorderSize);

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
