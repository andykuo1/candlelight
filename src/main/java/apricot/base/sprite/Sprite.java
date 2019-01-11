package apricot.base.sprite;

import apricot.base.asset.Asset;

import apricot.bstone.mogli.Texture;

/**
 * Created by Andy on 5/10/17.
 */
public final class Sprite
{
	private final Asset<Texture> texture;
	protected float u;
	protected float v;
	protected float width;
	protected float height;

	public Sprite(Asset<Texture> texture)
	{
		this.texture = texture;
		this.u = 0;
		this.v = 0;
		this.width = 1;
		this.height = 1;
	}

	public Sprite(Asset<Texture> texture, float u, float v, float width, float height)
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

	public Asset<Texture> getTexture()
	{
		return this.texture;
	}
}
