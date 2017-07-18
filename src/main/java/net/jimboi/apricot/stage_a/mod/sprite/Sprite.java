package net.jimboi.apricot.stage_a.mod.sprite;

import org.bstone.mogli.Texture;

/**
 * Created by Andy on 5/10/17.
 */
public class Sprite
{
	private final Texture texture;
	protected float u;
	protected float v;
	protected float width;
	protected float height;

	public Sprite(Texture texture, float u, float v, float width, float height)
	{
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}

	public float getU()
	{
		return this.u;
	}

	public float getV()
	{
		return this.v;
	}

	public float getWidth()
	{
		return this.width;
	}

	public float getHeight()
	{
		return this.height;
	}

	public Texture getTexture()
	{
		return this.texture;
	}
}
