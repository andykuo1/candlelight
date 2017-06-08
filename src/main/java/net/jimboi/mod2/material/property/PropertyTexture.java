package net.jimboi.mod2.material.property;

import net.jimboi.mod2.sprite.Sprite;

import org.bstone.mogli.Texture;
import org.qsilver.material.Property;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyTexture extends Property
{
	public Texture texture;
	public Sprite sprite;

	public PropertyTexture(Texture texture, Sprite sprite)
	{
		this.texture = texture;
		this.sprite = sprite;
	}

	public PropertyTexture(Texture texture)
	{
		this.texture = texture;
		this.sprite = new Sprite(this.texture, 0, 0, 1, 1);
	}
}
