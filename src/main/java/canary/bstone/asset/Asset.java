package canary.bstone.asset;

/**
 * Created by Andy on 10/26/17.
 */
public class Asset<T extends AutoCloseable>
{
	public static final long MAX_INACTIVE_TIME = 10000;

	protected final AssetManager manager;

	protected final String type;
	protected final String name;

	private boolean active;

	Asset(AssetManager manager, String type, String name)
	{
		this.manager = manager;
		this.type = type;
		this.name = name;

		this.active = true;
	}

	public T get()
	{
		return this.manager.getResource(this.type, this.name);
	}

	public void delete()
	{
		this.active = false;
	}

	public final boolean isActive()
	{
		return this.active;
	}

	public final String getType()
	{
		return this.type;
	}

	public final String getName()
	{
		return this.name;
	}

	public final AssetManager getManager()
	{
		return this.manager;
	}

	@Override
	public final boolean equals(Object o)
	{
		return o instanceof Asset
				&& this.type.equals(((Asset) o).type)
				&& this.name.equals(((Asset) o).name);
	}
}
