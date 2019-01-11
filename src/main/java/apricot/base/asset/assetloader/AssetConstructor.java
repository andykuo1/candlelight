package apricot.base.asset.assetloader;

import apricot.base.asset.Asset;
import apricot.base.asset.AssetManager;
import apricot.base.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
