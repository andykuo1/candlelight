package net.jimboi.mod.resource;

/**
 * Created by Andy on 4/8/17.
 */
public class StreamedResourcePointer<T extends Resource> extends ResourcePointer<T>
{
	private long lastActiveTime;
	private boolean active;

	StreamedResourcePointer(ResourceMap resourceMap, Class<T> type, ResourceLocation location, Object[] args)
	{
		super(resourceMap, type, location, args);

		this.lastActiveTime = 0;
		this.active = false;
	}

	@Override
	public boolean load()
	{
		boolean flag = super.load();
		if (flag) this.markActive();
		return flag;
	}

	@Override
	public void release()
	{
		this.active = false;
		super.release();
	}

	@Override
	public T getSource()
	{
		if (!this.active)
		{
			this.load();
		}
		else
		{
			this.markActive();
		}

		return super.getSource();
	}

	public void markActive()
	{
		this.active = true;
		this.lastActiveTime = System.currentTimeMillis();
	}

	public long getElapsedActiveTime()
	{
		return System.currentTimeMillis() - this.lastActiveTime;
	}

	public long getLastActiveTime()
	{
		return this.lastActiveTime;
	}

	public boolean isActive()
	{
		return this.active;
	}
}
