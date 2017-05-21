package net.jimboi.mod.resource;

/**
 * Created by Andy on 4/8/17.
 */
public class ResourcePointer<T extends Resource>
{
	public static boolean LOAD_ON_DEMAND = true;

	protected final ResourceMap resourceMap;

	protected ResourcePointer<? extends T> nullptr = null;
	protected boolean essential = LOAD_ON_DEMAND;//TODO: should NOT be loaded on demand

	protected final Class<T> type;
	protected final ResourceLocation location;
	protected final Object[] args;

	protected T source = null;

	ResourcePointer(ResourceMap resourceMap, Class<T> type, ResourceLocation location, Object[] args)
	{
		this.resourceMap = resourceMap;

		this.type = type;
		this.location = location;
		this.args = args;

		this.source = null;
	}

	public boolean load()
	{
		this.source = this.resourceMap.getResourceManager().load(this.type, this.location, this.args);

		return this.source != null;
	}

	public void release()
	{
		if (this.source != null)
		{
			this.resourceMap.getResourceManager().unload(this.location);
			this.source = null;
		}
	}

	public T getSource()
	{
		if (this.source == null && (!this.essential || !this.load()))
		{
			return this.nullptr.getSource();
		}

		return this.source;
	}

	public boolean isEssential()
	{
		return this.essential;
	}

	public void setEssential(boolean essential)
	{
		this.essential = essential;
	}

	public ResourcePointer<? extends T> getNullPointer()
	{
		return this.nullptr;
	}

	public void setNullPointer(ResourcePointer<? extends T> pointer)
	{
		this.nullptr = pointer;
	}

	public ResourceLocation getResourceLocation()
	{
		return this.location;
	}

	@Override
	public String toString()
	{
		return super.toString() + " : < " + (this.source == null ? this.type : this.source) + " >";
	}
}
