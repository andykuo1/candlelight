package net.jimboi.mod2.asset;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 4/19/17.
 */
public class AssetManager
{
	protected final Map<Class, Map<String, AutoCloseable>> assets = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> T getAsset(Class<? super T> assetType, String id)
	{
		Map<String, AutoCloseable> assetMap = this.getAssetMap(assetType);
		AutoCloseable asset = assetMap.get(id);
		if (asset == null)
			throw new IllegalArgumentException("Asset '" + assetType + "' with id '" + id + "' does not exist!");

		return (T) asset;
	}

	public <T extends AutoCloseable> void register(Class<? super T> assetType, String id, T source)
	{
		Map<String, AutoCloseable> assetMap = this.getAssetMap(assetType);
		if (assetMap.containsKey(id))
			throw new IllegalArgumentException("Asset '" + assetType + "' with id '" + id + "' already exists!");

		assetMap.put(id, source);
	}

	public void update()
	{

	}

	public void destroy()
	{
		for (Map<String, AutoCloseable> assetMap : this.assets.values())
		{
			for (AutoCloseable asset : assetMap.values())
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
			assetMap.clear();
		}
		this.assets.clear();
	}

	protected Map<String, AutoCloseable> getAssetMap(Class assetType)
	{
		return this.assets.computeIfAbsent(assetType, (key) -> new HashMap<>());
	}
}
