package canary.bstone.sprite;

import canary.bstone.asset.Asset;
import canary.bstone.mogli.Texture;
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
