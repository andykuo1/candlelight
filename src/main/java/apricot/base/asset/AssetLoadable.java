package apricot.base.asset;

/**
 * Created by Andy on 8/4/17.
 */
public final class AssetLoadable<T> extends Asset<T>
{
	protected final AssetManager assetManager;
	protected ResourceParameter<T> params;

	protected long lastActiveTime;

	public AssetLoadable(AssetManager assetManager, Class<T> type, String id, ResourceParameter<T> params)
	{
		super(type, id);
		this.assetManager = assetManager;
		this.params = params;
	}

	@Override
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
}
