package org.bstone.render.material;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.zilar.sprite.Sprite;

/**
 * Created by Andy on 8/12/17.
 */
public class ContextTexture extends Context
{
	public ContextTexture setSprite(Sprite sprite)
	{
		this.setTexture(sprite.getTexture());
		this.setSpriteOffset(new Vector2f(sprite.getU(), sprite.getV()));
		this.setSpriteScale(new Vector2f(sprite.getWidth(), sprite.getHeight()));
		return this;
	}

	public ContextTexture setTexture(Asset<Texture> texture)
	{
		this.material.setProperty(PropertyTexture.TEXTURE_NAME, texture);
		return this;
	}

	public ContextTexture setSpriteOffset(Vector2fc offset)
	{
		this.material.setProperty(PropertyTexture.SPRITE_OFFSET_NAME, offset);
		return this;
	}

	public ContextTexture setSpriteScale(Vector2fc scale)
	{
		this.material.setProperty(PropertyTexture.SPRITE_SCALE_NAME, scale);
		return this;
	}

	public ContextTexture setTransparency(boolean transparency)
	{
		this.material.setProperty(PropertyTexture.TRANSPARENCY_NAME, transparency);
		return this;
	}
}
