package canary.bstone.asset;

import canary.bstone.resource.ResourceLoader;
import canary.qsilver.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 10/26/17.
 */
public class AssetManager
{
	private final Set<Asset> assets = new HashSet<>();
	private final Map<String, ResourceLocation> locations = new HashMap<>();

	private final Map<String, ResourceLoader> loaders = new HashMap<>();
	private final Map<ResourceLoader, Map<String, AutoCloseable>> cached = new HashMap<>();

	public void update()
	{
		Iterator<Asset> iter = this.assets.iterator();
		while(iter.hasNext())
		{
			Asset asset = iter.next();
			if (!asset.isActive())
			{
				iter.remove();

				if (this.isResourceCached(asset.getType(), asset.getName()))
				{
					try
					{
						this.unloadResource(asset.getType(), asset.getName());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				//Update asset
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void destroy()
	{
		for(ResourceLoader loader : this.cached.keySet())
		{
			Map<String, AutoCloseable> cache = this.cached.get(loader);
			Iterator<AutoCloseable> iter = cache.values().iterator();
			while(iter.hasNext())
			{
				AutoCloseable resource = iter.next();
				try
				{
					if (loader != null)
					{
						loader.unload(resource);
					}
					else
					{
						resource.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				iter.remove();
			}
		}

		this.cached.clear();
	}

	@SuppressWarnings("unchecked")
	public void reload()
	{
		Set<String> resources = new HashSet<>();

		for(String type : this.loaders.keySet())
		{
			ResourceLoader loader = this.loaders.get(type);
			Map<String, AutoCloseable> cache = this.cached.get(this.loaders.get(type));
			Iterator<String> iter = cache.keySet().iterator();
			while(iter.hasNext())
			{
				String name = iter.next();
				resources.add(name);

				AutoCloseable resource = cache.get(name);
				try
				{
					if (loader != null)
					{
						loader.unload(resource);
					}
					else
					{
						resource.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				iter.remove();
			}

			for(String name : resources)
			{
				try
				{
					this.loadResource(type, name);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			resources.clear();
		}
	}

	public void registerResourceLocation(String id, ResourceLocation location)
	{
		this.locations.put(id, location);
	}

	public void unregisterResourceLocation(String id)
	{
		this.locations.remove(id);
	}

	public ResourceLocation getResourceLocation(String id)
	{
		return this.locations.get(id);
	}

	public void registerLoader(String type, ResourceLoader loader)
	{
		this.loaders.put(type, loader);
	}

	public void unregisterLoader(String type)
	{
		this.loaders.remove(type);
	}

	public ResourceLoader getLoader(String type)
	{
		return this.loaders.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> Asset<T> getAsset(String type, String name)
	{
		Asset<T> asset = new Asset<>(this, type, name);
		this.assets.add(asset);
		return asset;
	}

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> T getResource(String type, String name)
	{
		ResourceLoader loader = this.getLoader(type);
		if (this.cached.containsKey(loader))
		{
			AutoCloseable resource = this.cached.get(loader).get(name);
			if (resource != null) return (T) resource;
		}

		try
		{
			return this.loadResource(type, name);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public boolean isResourceCached(String type, String name)
	{
		ResourceLoader loader = this.getLoader(type);
		return this.cached.containsKey(loader) && this.cached.get(loader).containsKey(name);
	}

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> T loadResource(String type, String name) throws Exception
	{
		ResourceLoader loader = this.getLoader(type);
		if (loader == null) throw new IllegalArgumentException("cannot find resource loader for type '" + type + "'");

		AutoCloseable resource;
		try (InputStream stream = this.getResourceStream(type, name))
		{
			resource = loader.load(stream);
		}
		return this.cacheResource(type, name, (T) resource);
	}

	@SuppressWarnings("unchecked")
	public void unloadResource(String type, String name) throws Exception
	{
		ResourceLoader loader = this.getLoader(type);
		if (this.cached.containsKey(loader))
		{
			AutoCloseable resource = this.cached.get(loader).remove(name);
			if (resource != null)
			{
				if (loader != null)
				{
					loader.unload(resource);
				}
				else
				{
					resource.close();
				}
				return;
			}
		}

		throw new IllegalArgumentException("cannot find loaded resource with name '" + name + "' of type '" + type + "'");
	}

	public <T extends AutoCloseable> T reloadResource(String type, String name) throws Exception
	{
		this.unloadResource(type, name);
		return this.loadResource(type, name);
	}

	public <T extends AutoCloseable> T cacheResource(String type, String name, T resource)
	{
		ResourceLoader loader = this.getLoader(type);

		Map<String, AutoCloseable> cache = this.cached.computeIfAbsent(loader, k -> new HashMap<>());
		if (cache.containsKey(name))
		{
			throw new IllegalArgumentException("resource '" + type + "' with name '" + name + "' already cached!");
		}
		cache.put(name, resource);
		return resource;
	}

	public InputStream getResourceStream(String type, String name) throws IOException
	{
		ResourceLocation location = this.locations.get(type + "." + name);
		if (location == null) throw new IllegalArgumentException("cannot find resource '" + type + "' with name '" + name + "' - resource not registered");
		Path path = Paths.get(location.getFilePath());
		if (!Files.exists(path))
		{
			throw new IllegalArgumentException("cannot find resource '" + type + "' with name '" + name + "' - file does not exist");
		}
		return Files.newInputStream(path);
	}

	public static long getCurrentTime()
	{
		return System.currentTimeMillis();
	}
}
