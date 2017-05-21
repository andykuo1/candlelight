package net.jimboi.mod.resource;

import org.bstone.util.poma.Poma;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 4/6/17.
 */
public class ResourceManager
{
	private final Map<ResourceLocation, Resource> resources = new HashMap<>();
	private final Set<ResourceMap> mappings = new HashSet<>();

	public void update()
	{
		for(ResourceMap map : this.mappings)
		{
			map.update();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Resource> T load(Class<T> type, ResourceLocation location, final Object... args)
	{
		Resource r = this.resources.computeIfAbsent(location, (key)->{
			try
			{
				Poma.d("Resource Loaded: " + location + " with " + args.length + " args : " + Arrays.asList(args));

				Resource r2 = type.getDeclaredConstructor(ResourceLocation.class, Object[].class).newInstance(location, args);
				this.resources.put(key, r2);
				return r2;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		});

		r.refcount++;
		return (T) r;
	}

	public void unload(ResourceLocation location)
	{
		Resource r = this.resources.get(location);
		r.refcount--;
		if (r.refcount == 0)
		{
			Poma.d("Resource Unloaded: " + location);

			r.destroy();
			this.resources.remove(location);
		}
	}

	public ResourceMap createResourceMapping(String name)
	{
		return new ResourceMap(this, name);
	}
}
