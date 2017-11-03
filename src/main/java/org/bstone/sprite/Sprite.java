package org.bstone.sprite;

import org.bstone.asset.Asset;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;

/**
 * Created by Andy on 10/31/17.
 */
public abstract class Sprite
{
	public abstract Vector2f getUV(Vector2f dst);

	public abstract Vector2f getSize(Vector2f dst);

	public abstract Asset<Texture> getTexture();
}
