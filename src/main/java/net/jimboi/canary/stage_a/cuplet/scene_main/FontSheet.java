package net.jimboi.canary.stage_a.cuplet.scene_main;

import org.bstone.asset.Asset;
import org.bstone.sprite.textureatlas.SubTexture;
import org.bstone.sprite.textureatlas.TextureAtlas;

/**
 * Created by Andy on 10/31/17.
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

	public SubTexture get(char c)
	{
		if (this.isValid(c))
		{
			return this.textureAtlas.get().getSubTexture(c + this.spriteOffset);
		}

		return this.get(this.minChar);
	}

	public boolean isValid(char c)
	{
		return c >= this.minChar && c <= this.maxChar;
	}
}
