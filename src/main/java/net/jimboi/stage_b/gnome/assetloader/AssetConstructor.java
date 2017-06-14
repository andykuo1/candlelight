package net.jimboi.stage_b.gnome.assetloader;

import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface AssetConstructor<T>
{
	Asset<T> create(AssetManager assetManager, Class assetType, String assetID, ResourceParameter params);
}
