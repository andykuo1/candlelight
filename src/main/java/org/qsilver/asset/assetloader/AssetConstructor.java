package org.qsilver.asset.assetloader;

import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
