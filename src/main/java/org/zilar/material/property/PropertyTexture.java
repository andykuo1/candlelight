package org.zilar.material.property;

import org.bstone.material.Property;
import org.bstone.mogli.Texture;
import org.zilar.asset.Asset;
import org.zilar.sprite.Sprite;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyTexture extends Property
{
	public Asset<Texture> texture;
	public Sprite sprite;
	public boolean transparent = false;

	public PropertyTexture(Asset<Texture> texture, Sprite sprite)
	{
		this.texture = texture;
		this.sprite = sprite;
	}

	public PropertyTexture(Asset<Texture> texture)
	{
		this.texture = texture;
		this.sprite = new Sprite(this.texture, 0, 0, 1, 1);
	}

	public PropertyTexture setTransparent(boolean transparent)
	{
		this.transparent = transparent;
		return this;
	}
}
