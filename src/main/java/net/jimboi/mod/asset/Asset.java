package net.jimboi.mod.asset;

/**
 * Created by Andy on 4/19/17.
 */
public class Asset<T extends AutoCloseable> implements AutoCloseable
{
	private final AssetManager assetManager;

	private T source;

	public Asset(AssetManager assetManager, T source)
	{
		this.assetManager = assetManager;
		this.source = source;
	}

	public T getSource()
	{
		return this.source;
	}

	@Override
	public void close() throws Exception
	{
		if (this.source != null)
		{
			this.source.close();
		}
		this.source = null;
	}
}
