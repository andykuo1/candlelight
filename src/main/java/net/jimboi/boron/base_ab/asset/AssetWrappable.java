package net.jimboi.boron.base_ab.asset;

/**
 * Created by Andy on 8/4/17.
 */
public final class AssetWrappable<T> extends Asset<T>
{
	private final T source;

	AssetWrappable(Class<T> type, T source, String id)
	{
		super(type, id);

		this.source = source;
	}

	@Override
	public T getSource()
	{
		return this.source;
	}
}
