package net.jimboi.boron.base_ab.asset;

/**
 * Created by Andy on 6/9/17.
 */
public abstract class Asset<T>
{
	@SuppressWarnings("unchecked")
	public static <T> AssetWrappable<T> wrap(T source)
	{
		return new AssetWrappable<>((Class<T>) source.getClass(), source, source.toString());
	}

	protected final Class<T> type;
	protected String id;

	public Asset(Class<T> type, String id)
	{
		this.type = type;

		this.id = id;
	}

	public abstract T getSource();

	public final Class<T> getType()
	{
		return this.type;
	}

	public final String getID()
	{
		return this.id;
	}

	@Override
	public final boolean equals(Object o)
	{
		if (o instanceof Asset)
		{
			Asset asset = (Asset) o;
			return this.type.equals(asset.type) && this.id.equals(asset.id);
		}

		return false;
	}

	@Override
	public final String toString()
	{
		return this.type.getSimpleName().toLowerCase() + ":" + this.id;
	}
}
