package org.qsilver.asset;

/**
 * Created by Andy on 6/9/17.
 */
public final class Asset<T>
{
	protected final AssetManager assetManager;
	protected final Class<T> type;

	protected String id;
	protected ResourceParameter<T> params;

	protected long lastActiveTime;

	public Asset(AssetManager assetManager, Class<T> type, String id, ResourceParameter<T> params)
	{
		this.assetManager = assetManager;
		this.type = type;

		this.id = id;
		this.params = params;
	}

	public T getSource()
	{
		if (this.params == null) throw new IllegalStateException("Unable to load unsafe asset!");

		this.lastActiveTime = this.assetManager.getCurrentTime();
		return this.assetManager.loadResource(this.type, this.id, this.params);
	}

	public long getLastActiveTime()
	{
		return this.lastActiveTime;
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
	public boolean equals(Object o)
	{
		if (o instanceof Asset)
		{
			Asset asset = (Asset) o;
			return this.type.equals(asset.type) && this.id.equals(asset.id);
		}

		return false;
	}

	@Override
	public String toString()
	{
		return this.type.getSimpleName().toLowerCase() + ":" + this.id;
	}
}
