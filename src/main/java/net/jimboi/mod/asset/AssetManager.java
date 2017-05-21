package net.jimboi.mod.asset;

import java.util.Map;

/**
 * Created by Andy on 4/19/17.
 */
public class AssetManager
{
	private Map<String, Asset> assets;
	private Map<Class, String> defaults;

	public <T extends AutoCloseable> Asset<T> register(String id, T source)
	{
		if (this.assets.containsKey(id))
		{
			throw new IllegalArgumentException("Asset with id '" + id + "' already exists!");
		}

		Asset<T> asset = new Asset<>(this, source);
		this.assets.put(id, asset);
		return asset;
	}

	public void remove(String id)
	{
		Asset asset = this.assets.remove(id);
		if (asset != null)
		{
			try
			{
				asset.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void markAssetAsDefault(String id, Class<? extends AutoCloseable> type)
	{
		this.defaults.put(type, id);
	}

	public <T extends AutoCloseable> Asset<T> getAsset(Class<T> type, String id, boolean useDefault)
	{
		Asset asset = this.assets.get(id);
		if (asset == null)
		{
			if (!useDefault)
			{
				throw new IllegalArgumentException("Could not find asset with id '" + id + "'");
			}
			else
			{
				return getAsset(type);
			}
		}
		return asset;
	}

	public <T extends AutoCloseable> Asset<T> getAsset(Class<T> type, String id)
	{
		return getAsset(type, id, true);
	}

	public <T extends AutoCloseable> Asset<T> getAsset(Class<T> type)
	{
		String id = this.defaults.get(type);
		if (id == null)
		{
			throw new IllegalArgumentException("Could not find default asset of type '" + type + "'");
		}
		return this.getAsset(type, id, false);
	}

	public void clear()
	{
		for(Asset asset : this.assets.values())
		{
			try
			{
				asset.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.assets.clear();
		this.defaults.clear();
	}
}
