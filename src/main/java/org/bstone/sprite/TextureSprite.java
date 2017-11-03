package org.bstone.sprite;

import org.bstone.asset.Asset;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;

/**
 * Created by Andy on 10/31/17.
 */
public class TextureSprite extends Sprite
{
	protected final Asset<Texture> texture;

	public TextureSprite(Asset<Texture> texture)
	{
		this.texture = texture;
	}

	@Override
	public Vector2f getUV(Vector2f dst)
	{
		return dst.set(0, 0);
	}

	@Override
	public Vector2f getSize(Vector2f dst)
	{
		return dst.set(1, 1);
	}

	@Override
	public Asset<Texture> getTexture()
	{
		return this.texture;
	}
}
