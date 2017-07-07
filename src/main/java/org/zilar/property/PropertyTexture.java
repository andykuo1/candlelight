package org.zilar.property;

import org.bstone.material.Property;
import org.bstone.mogli.Texture;
import org.qsilver.asset.Asset;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.SpriteSheet;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyTexture extends Property
{
	private SpriteSheet spritesheet;
	private Sprite sprite;
	private Asset<Texture> texture;

	public boolean transparent = false;

	public PropertyTexture(SpriteSheet spritesheet)
	{
		this.spritesheet = spritesheet;
		this.sprite = this.spritesheet.get();
		this.texture = this.sprite.getTexture();
	}

	public PropertyTexture(Sprite sprite)
	{
		this.sprite = sprite;
		this.texture = this.sprite.getTexture();
	}

	public PropertyTexture(Asset<Texture> texture)
	{
		this.texture = texture;
		this.sprite = new Sprite(this.texture, 0, 0, 1, 1);
	}

	public void setSpriteSheet(SpriteSheet spritesheet)
	{
		this.spritesheet = spritesheet;
		this.sprite = this.spritesheet.get();
		this.texture = this.sprite.getTexture();
	}

	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
		this.texture = this.sprite.getTexture();
	}

	public SpriteSheet getSpriteSheet()
	{
		return this.spritesheet;
	}

	public Sprite getSprite()
	{
		return this.spritesheet != null ? this.spritesheet.get() : this.sprite;
	}

	public Asset<Texture> getTexture()
	{
		return this.spritesheet != null ? this.spritesheet.get().getTexture() : this.sprite.getTexture();
	}

	public PropertyTexture setTransparent(boolean transparent)
	{
		this.transparent = transparent;
		return this;
	}
}
