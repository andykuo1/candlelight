package net.jimboi.boron.base_ab.asset.assetloader;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.asset.AssetManager;
import net.jimboi.boron.base_ab.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
