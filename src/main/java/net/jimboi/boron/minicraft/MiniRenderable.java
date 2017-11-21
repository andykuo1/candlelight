package net.jimboi.boron.minicraft;

import net.jimboi.boron.base_ab.sprite.Sprite;

import org.bstone.mogli.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Created by Andy on 8/3/17.
 */
public class MiniRenderable
{
	public boolean visible = true;
	public Sprite sprite;
	public Mesh mesh;
	public boolean transparent = true;
	public final Vector4f color = new Vector4f(1, 1, 1, 0);

	public float x;
	public float y;

	public boolean isScreen;

	public MiniRenderable copy()
	{
		MiniRenderable ret = new MiniRenderable();
		ret.visible = visible;
		ret.sprite = sprite;
		ret.mesh = mesh;
		ret.transparent = transparent;
		ret.color.set(color);
		ret.x = x;
		ret.y = y;
		return ret;
	}

	public Matrix4f getRenderTransformation(Matrix4f dst)
	{
		if (this.isScreen)
		{
			//TODO: THIS IS HARDCODED!
			return dst.identity().translation(x, y, 0).scale(20, 20, 1);
		}
		else
		{
			return dst.identity().translation(x, y, 0);
		}
	}

	public Mesh getMesh()
	{
		return this.mesh;
	}

	public boolean getTransparent()
	{
		return this.transparent;
	}

	public Vector4f getColor()
	{
		return this.color;
	}

	public Sprite getSprite()
	{
		return this.sprite;
	}

	public boolean isVisible()
	{
		return this.visible;
	}
}
