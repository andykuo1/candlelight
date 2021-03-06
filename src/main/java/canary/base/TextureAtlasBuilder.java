package canary.base;

import canary.bstone.asset.Asset;
import canary.bstone.mogli.Texture;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/31/17.
 */
public class TextureAtlasBuilder
{
	private final List<Vector4f> subtextures = new ArrayList<>();

	private Asset<Texture> texture;
	private int width;
	private int height;

	public TextureAtlasBuilder(Asset<Texture> texture, int textureWidth, int textureHeight)
	{
		this.texture = texture;
		this.width = textureWidth;
		this.height = textureHeight;
	}

	public void setTexture(Asset<Texture> texture, int textureWidth, int textureHeight)
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

		this.subtextures.add(new Vector4f(1 - u - w, 1 - v - h, w, h));

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

	public TextureAtlas bake()
	{
		TextureAtlas result = new TextureAtlas(this.texture, this.subtextures.size());
		for(int i = 0; i < result.length(); ++i)
		{
			Vector4f vec = this.subtextures.get(i);
			SubTexture subtexture = result.getSubTexture(i);
			subtexture.setUV(vec.x(), vec.y());
			subtexture.setSize(vec.z(), vec.w());
		}
		return result;
	}

	public void clear()
	{
		this.subtextures.clear();

		this.texture = null;
		this.width = 0;
		this.height = 0;
	}
}
