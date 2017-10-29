package net.jimboi.boron.base_ab.asset.assetloader;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.asset.AssetManager;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 6/12/17.
 */
public class AssetTypes
{
	private static final Map<Class, AssetFunction> ASSET_TYPES = new HashMap<>();

	public static void clear()
	{
		ASSET_TYPES.clear();
	}

	public static <T> void registerAssetType(Class<T> type, AssetConstructor<T> assetProducer, ResourceParameterProducer<T> paramProducer)
	{
		if (ASSET_TYPES.containsKey(type))
			throw new IllegalArgumentException("Asset type '" + type.getSimpleName() + "' is already registered!");

		ASSET_TYPES.put(type, new AssetFunction<T>(assetProducer, paramProducer));
	}

	public static void removeAssetType(Class assetType)
	{
		if (!ASSET_TYPES.containsKey(assetType))
			throw new IllegalArgumentException("Asset type '" + assetType.getSimpleName() + "' is not yet registered!");

		ASSET_TYPES.remove(assetType);
	}

	public static Class getAssetType(String assetType)
	{
		for (Class src : ASSET_TYPES.keySet())
		{
			if (src.getSimpleName().toLowerCase().equals(assetType.toLowerCase()))
			{
				return src;
			}
		}

		throw new IllegalArgumentException("Invalid asset type - could not find asset type '" + assetType + "'!");
	}

	public static ResourceParameter getAssetParameter(Class assetType, Object[] args)
	{
		validateAssetType(assetType);

		return ASSET_TYPES.get(assetType).producer.create(assetType, args);
	}

	public static Asset getAsset(AssetManager assetManager, Class assetType, String id, ResourceParameter params)
	{
		validateAssetType(assetType);

		return ASSET_TYPES.get(assetType).constructor.create(assetManager, assetType, id, params);
	}

	public static boolean contains(Class assetType)
	{
		return ASSET_TYPES.containsKey(assetType);
	}

	protected static void validateAssetType(Class assetType)
	{
		if (ASSET_TYPES.containsKey(assetType)) return;

		throw new IllegalArgumentException("Invalid asset type '" + assetType.getSimpleName() + "' (asset types must be registered)!");
	}

	private static final class AssetFunction<T>
	{
		final AssetConstructor<T> constructor;
		final ResourceParameterProducer<T> producer;

		AssetFunction(AssetConstructor<T> constructor, ResourceParameterProducer<T> producer)
		{
			this.constructor = constructor;
			this.producer = producer;
		}
	}

}
