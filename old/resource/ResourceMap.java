package net.jimboi.mod.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 4/7/17.
 */
public class ResourceMap
{
	private final ResourceManager resourceManager;
	private final String name;

	private final Map<String, ResourcePointer> resourceMapping = new HashMap<>();

	private int maxInactiveTime = 100;

	ResourceMap(ResourceManager resourceManager, String name)
	{
		this.resourceManager = resourceManager;
		this.name = name;
	}

	public void update()
	{
		for(ResourcePointer rp : this.resourceMapping.values())
		{
			if (rp instanceof StreamedResourcePointer)
			{
				StreamedResourcePointer srp = (StreamedResourcePointer) rp;
				if (srp.getElapsedActiveTime() >= this.maxInactiveTime)
				{
					srp.release();
				}
			}
		}
	}

	public <T extends Resource> ResourcePointer<T> add(Class<T> type, String id, ResourceLocation location, Object... args)
	{
		return this.add(type, id, location, args, false);
	}

	@SuppressWarnings("unchecked")
	public <T extends Resource> ResourcePointer<T> add(Class<T> type, String id, ResourceLocation location, Object[] args, boolean streamed)
	{
		return (ResourcePointer<T>) this.resourceMapping.computeIfAbsent(id, (key)->{
			ResourcePointer rp = streamed ? new StreamedResourcePointer(ResourceMap.this, type, location, args) : new ResourcePointer(ResourceMap.this, type, location, args);
			resourceMapping.put(id, rp);
			return rp;
		});
	}

	public boolean remove(String id)
	{
		ResourcePointer rp = this.resourceMapping.remove(id);
		if (rp != null)
		{
			rp.release();
			return true;
		}
		return false;
	}

	public void clear()
	{
		for(ResourcePointer rp : this.resourceMapping.values())
		{
			rp.release();
		}

		this.resourceMapping.clear();
	}

	@SuppressWarnings("unchecked")
	public <T extends Resource> ResourcePointer<T> get(Class<T> type, String id)
	{
		return (ResourcePointer<T>) this.resourceMapping.get(id);
	}

	public void setMaxInactiveTime(int time)
	{
		this.maxInactiveTime = time;
	}

	public int getMaxInactiveTime()
	{
		return this.maxInactiveTime;
	}

	public ResourceManager getResourceManager()
	{
		return this.resourceManager;
	}

	public String getName()
	{
		return this.name;
	}
}
