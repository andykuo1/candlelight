package canary.bstone.sprite.textureatlas;

/**
 * Created by Andy on 10/31/17.
 */
public class SubTexture
{
	private final TextureAtlas atlas;

	protected float u;
	protected float v;
	protected float width;
	protected float height;

	SubTexture(TextureAtlas textureAtlas)
	{
		this(textureAtlas, 0, 0, 1, 1);
	}

	SubTexture(TextureAtlas textureAtlas, float u, float v, float width, float height)
	{
		this.atlas = textureAtlas;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}

	public void setUV(float u, float v)
	{
		this.u = u;
		this.v = v;
	}

	public void setSize(float width, float height)
	{
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

	public TextureAtlas getTextureAtlas()
	{
		return this.atlas;
	}
}
