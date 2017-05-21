package net.jimboi.mod.resource;

/**
 * Created by Andy on 4/6/17.
 */
public abstract class Resource
{
	protected final ResourceLocation location;
	protected final Object[] args;

	protected int refcount = 0;

	public Resource(ResourceLocation location, Object... args)
	{
		this.location = location;
		this.args = args;
	}

	public abstract void destroy();

	public ResourceLocation getResourceLocation()
	{
		return this.location;
	}
}
