package boron.base.asset;

import java.util.function.Supplier;

/**
 * Created by Andy on 10/30/17.
 */
public final class AssetLateWrappable<T> extends Asset<T>
{
	private final Supplier<T> source;
	private T data;

	AssetLateWrappable(Class<T> type, Supplier<T> source, String id)
	{
		super(type, id);

		this.source = source;
	}

	@Override
	public T getSource()
	{
		if (this.data == null)
		{
			this.data = this.source.get();
		}
		return this.data;
	}
}
