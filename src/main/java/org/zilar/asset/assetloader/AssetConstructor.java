package org.zilar.asset.assetloader;

import org.zilar.asset.Asset;
import org.zilar.asset.AssetManager;
import org.zilar.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
