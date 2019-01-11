package apricot.base.sprite;

import apricot.base.asset.Asset;

/**
 * Created by Andy on 7/17/17.
 */
public class FontSheet
{
	private Asset<TextureAtlas> textureAtlas;
	private int spriteOffset;
	private char minChar;
	private char maxChar;

	public FontSheet(Asset<TextureAtlas> textureAtlas, int spriteOffset, char minChar, char maxChar)
	{
		this.textureAtlas = textureAtlas;

		this.spriteOffset = spriteOffset;
		this.minChar = minChar;
		this.maxChar = maxChar;
	}

	public Sprite get(char c)
	{
		if (this.isValid(c))
		{
			return this.textureAtlas.getSource().getSprite(c + this.spriteOffset);
		}

		return this.get(this.minChar);
	}

	public boolean isValid(char c)
	{
		return c >= this.minChar && c <= this.maxChar;
	}
}
