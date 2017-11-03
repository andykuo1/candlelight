package org.bstone.material.property;

import org.bstone.asset.Asset;
import org.bstone.mogli.Program;

/**
 * Created by Andy on 11/1/17.
 */
public abstract class PropertyAsset<T extends AutoCloseable> extends Property<Asset>
{
	private final String assetType;

	public PropertyAsset(String assetType, String name)
	{
		super(Asset.class, name);

		this.assetType = assetType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void apply(Program program, Asset value)
	{
		this.apply(program, (T) value.get());
	}

	public abstract void apply(Program program, T value);

	@Override
	public boolean isSupportedValue(Object value)
	{
		return super.isSupportedValue(value) && this.assetType.equals(((Asset) value).getType());
	}

	public final String getAssetType()
	{
		return this.assetType;
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj) && obj instanceof PropertyAsset && this.assetType.equals(((PropertyAsset) obj).assetType);
	}
}
