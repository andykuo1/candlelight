package org.qsilver.asset;

import net.jimboi.boron.base.render.OldRenderEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 4/19/17.
 */
public class AssetManager
{
	protected final Map<Class, Map<String, Asset>> assets = new HashMap<>();
	protected final Map<String, Object> resources = new HashMap<>();
	protected final Map<Class, ResourceLoader> loaders = new HashMap<>();

	protected int maxInactiveTime = 100000;

	private int tickCounter;
	private int tickRate = 100;

	public <T> void registerLoader(Class<T> assetType, ResourceLoader<T, ? extends ResourceParameter<T>> loader)
	{
		if (this.loaders.containsKey(assetType))
			throw new IllegalArgumentException("Loader for asset type '" + assetType.getSimpleName() + "' already exists!");
		this.loaders.put(assetType, loader);
	}

	public <T> ResourceLoader getLoader(Class<T> assetType)
	{
		return this.loaders.get(assetType);
	}

	@SuppressWarnings("unchecked")
	public <T> Asset<T> registerAsset(Class<T> assetType, String id, ResourceParameter<T> params)
	{
		Map<String, Asset> assetMap = this.getAssetMap(assetType);
		Asset asset = assetMap.get(id);
		if (asset != null)
		{
			throw new IllegalArgumentException("Asset '" + assetType.getSimpleName() + "' with id '" + id + "' already exists!");

			/*
			if (!assetType.equals(asset.getType()))
				throw new IllegalStateException("Asset '" + assetType.getSimpleName() + "' does not match type '" + asset.getType().getSimpleName() + "' which already exists!");

			if (asset instanceof AssetLoadable)
			{
				((AssetLoadable) asset).params = params;
				asset.id = id;
			}

			//Missing AssetWrappable
			*/
		}
		else
		{
			asset = new AssetLoadable<>(this, assetType, id, params);
			assetMap.put(id, asset);
		}
		return asset;
	}

	public boolean registerAsset(Class assetType, String id, Asset asset)
	{
		Map<String, Asset> assetMap = this.getAssetMap(assetType);
		Asset other = assetMap.get(id);
		if (other != null)
		{
			throw new IllegalArgumentException("Asset '" + assetType.getSimpleName() + "' with id '" + id + "' already exists!");

			/*
			if (!assetType.equals(asset.getType()))
				throw new IllegalStateException("Asset '" + assetType.getSimpleName() + "' does not match type '" + other.getType().getSimpleName() + "' which already exists!");
			*/
		}
		else
		{
			assetMap.put(id, asset);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Asset<T> registerAsset(Class<T> assetType, T source, String id)
	{
		Map<String, Asset> assetMap = this.getAssetMap(assetType);
		Asset asset = assetMap.get(id);
		if (asset != null)
		{
			throw new IllegalArgumentException("Asset '" + assetType.getSimpleName() + "' with id '" + id + "' already exists!");

			/*
			if (!assetType.equals(asset.getType()))
				throw new IllegalStateException("Asset '" + assetType.getSimpleName() + "' does not match type '" + asset.getType().getSimpleName() + "' which already exists!");

			asset = new AssetLoadable<>(this, assetType, id, params);
			assetMap.put(id, asset);
			*/
		}
		else
		{
			asset = new AssetWrappable<>(assetType, source, id);
			assetMap.put(id, asset);
		}
		return asset;
	}

	public boolean containsAsset(Class assetType, String id)
	{
		return this.assets.containsKey(assetType) && this.assets.get(assetType).containsKey(id);
	}

	@SuppressWarnings("unchecked")
	public <T> Asset<T> getAsset(Class<T> assetType, String id)
	{
		Map<String, Asset> assetMap = this.getAssetMap(assetType);
		Asset asset = assetMap.get(id);
		if (asset == null)
			throw new IllegalArgumentException("Asset '" + assetType.getSimpleName() + "' with id '" + id + "' does not exist!");
		if (!assetType.equals(asset.getType()))
			throw new IllegalArgumentException("Asset '" + assetType.getSimpleName() + "' does not match type '" + asset.getType().getSimpleName() + "'!");

		return asset;
	}

	public Asset getUnsafeAsset(Class assetType, String id)
	{
		return this.getAssetMap(assetType).get(id);
	}

	@SuppressWarnings("unchecked")
	public <T, P extends ResourceParameter<T>> T loadResource(Class<T> assetType, String id, P params)
	{
		String resourceID = this.getResourceID(assetType, id);
		Object resource = this.resources.get(resourceID);
		if (resource == null)
		{
			ResourceLoader<T, P> loader = this.loaders.get(assetType);
			resource = loader.load(params);
			this.resources.put(resourceID, resource);
		}
		return (T) resource;
	}

	public void unloadResource(Class assetType, String id)
	{
		String resourceID = this.getResourceID(assetType, id);
		Object resource = this.resources.remove(resourceID);
		if (resource instanceof AutoCloseable)
		{
			try
			{
				((AutoCloseable) resource).close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean isResourceLoaded(Class assetType, String id)
	{
		String resourceID = this.getResourceID(assetType, id);
		return this.resources.containsKey(resourceID);
	}

	public void update(OldRenderEngine renderEngine)
	{
		if (++this.tickCounter < this.tickRate) return;
		this.tickCounter = 0;

		if (this.maxInactiveTime >= 0)
		{
			long time = this.getCurrentTime();
			for (Map<String, Asset> assetMap : this.assets.values())
			{
				for (Asset<?> asset : assetMap.values())
				{
					if (!(asset instanceof AssetLoadable)) continue;
					AssetLoadable loadable = (AssetLoadable) asset;

					if (loadable.getLastActiveTime() > 0 && time - loadable.getLastActiveTime() >= this.maxInactiveTime)
					{
						this.unloadResource(asset.getType(), asset.getID());
					}
				}
			}
		}
	}

	public void destroy()
	{
		for(Class assetType : this.assets.keySet())
		{
			Map<String, Asset> assetMap = this.assets.get(assetType);
			for(String id : assetMap.keySet())
			{
				this.unloadResource(assetType, id);
			}
		}

		this.assets.clear();
		this.resources.clear();
		this.loaders.clear();
	}

	public long getCurrentTime()
	{
		return System.currentTimeMillis();
	}

	protected String getResourceID(Class assetType, String id)
	{
		return assetType.getSimpleName().toLowerCase() + ":" + id;
	}

	protected Map<String, Asset> getAssetMap(Class assetType)
	{
		return this.assets.computeIfAbsent(assetType, (key) -> new HashMap<>());
	}
}
