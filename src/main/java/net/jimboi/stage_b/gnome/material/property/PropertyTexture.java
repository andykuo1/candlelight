package net.jimboi.stage_b.gnome.material.property;

import net.jimboi.stage_b.gnome.sprite.Sprite;

import org.bstone.material.Property;
import org.bstone.mogli.Texture;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyTexture extends Property
{
	public Texture texture;
	public Sprite sprite;
	public boolean transparent = false;

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
