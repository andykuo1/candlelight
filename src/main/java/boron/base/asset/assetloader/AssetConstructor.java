package boron.base.asset.assetloader;

import boron.base.asset.Asset;
import boron.base.asset.AssetManager;
import boron.base.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
