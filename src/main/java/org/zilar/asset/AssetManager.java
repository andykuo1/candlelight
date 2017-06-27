package org.zilar.asset;

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
			if (!assetType.equals(asset.getType()))
				throw new IllegalStateException("Asset '" + assetType.getSimpleName() + "' does not match type '" + asset.getType().getSimpleName() + "' which already exists!");

			asset.params = params;
			asset.id = id;
		}
		else
		{
			asset = new Asset<>(this, assetType, id, params);
			assetMap.put(id, asset);
		}
		return asset;
	}

	public boolean registerAsset(Class assetType, String id, Asset asset, boolean overwrite)
	{
		Map<String, Asset> assetMap = this.getAssetMap(assetType);
		Asset other = assetMap.get(id);
		if (other != null)
		{
			if (!assetType.equals(asset.getType()))
				throw new IllegalStateException("Asset '" + assetType.getSimpleName() + "' does not match type '" + other.getType().getSimpleName() + "' which already exists!");

			if (overwrite)
			{
				other.params = asset.params;
				other.id = id;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			assetMap.put(id, asset);
		}
		return true;
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

	public void update()
	{
	}

	public void destroy()
	{
		this.assets.clear();

		for (Object resource : this.resources.values())
		{
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
		this.resources.clear();
		this.loaders.clear();
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
