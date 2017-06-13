package net.jimboi.stage_b.gnome.asset;

/**
 * Created by Andy on 6/9/17.
 */
public final class Asset<T>
{
	protected final AssetManager assetManager;
	protected final Class<T> type;

	protected String id;
	protected ResourceParameter<T> params;

	public Asset(AssetManager assetManager, Class<T> type, String id, ResourceParameter<T> params)
	{
		this.assetManager = assetManager;
		this.type = type;

		this.id = id;
		this.params = params;
	}

	public T getSource()
	{
		return this.assetManager.loadResource(this.type, this.id, this.params);
	}

	public ResourceParameter<T> getParameters()
	{
		return this.params;
	}

	public Class<T> getType()
	{
		return this.type;
	}

	public String getID()
	{
		return this.id;
	}

	@Override
	public String toString()
	{
		return this.type.getSimpleName().toLowerCase() + ":" + this.id;
	}
}
