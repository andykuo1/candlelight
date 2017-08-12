package net.jimboi.apricot.base.renderer.property;

import net.jimboi.apricot.base.material.OldProperty;

import org.bstone.mogli.Texture;
import org.qsilver.asset.Asset;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.SpriteSheet;

/**
 * Created by Andy on 6/8/17.
 */
public class OldPropertyTexture extends OldProperty
{
	private SpriteSheet spritesheet;
	private Sprite sprite;
	private Asset<Texture> texture;

	private boolean transparent = false;

	public OldPropertyTexture(SpriteSheet spritesheet)
	{
		this.spritesheet = spritesheet;
		this.sprite = this.spritesheet.get();
		this.texture = this.sprite.getTexture();
	}

	public OldPropertyTexture(Sprite sprite)
	{
		this.sprite = sprite;
		this.texture = this.sprite.getTexture();
	}

	public OldPropertyTexture(Asset<Texture> texture)
	{
		this.texture = texture;
		this.sprite = new Sprite(this.texture, 0, 0, 1, 1);
	}

	public OldPropertyTexture setTransparent(boolean transparent)
	{
		this.transparent = transparent;
		return this;
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

	public boolean isTransparent()
	{
		return this.transparent;
	}
}
