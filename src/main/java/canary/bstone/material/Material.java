package canary.bstone.material;

import canary.bstone.asset.Asset;
import canary.bstone.material.property.Property;
import canary.bstone.material.property.PropertyAsset;
import canary.bstone.mogli.Program;
import canary.bstone.util.small.SmallMap;

import java.util.Map;

/**
 * Created by Andy on 10/31/17.
 */
public class Material
{
	private final Map<Property, Object> properties = new SmallMap<>();

	private Material parent;

	public Material setParent(Material material)
	{
		this.parent = material;
		return this;
	}

	public <T> boolean apply(Property<T> property, Program program)
	{
		T value = this.getProperty(property);
		if (value == null) return false;

		property.apply(program, value);
		return true;
	}

	public <T> void applyOrDefault(Property<T> property, Program program, T defaultValue)
	{
		T value = this.getProperty(property);
		if (value == null)
		{
			value = defaultValue;
		}

		property.apply(program, value);
	}

	public <T> void setProperty(Property<T> property, T value)
	{
		if (value != null && !property.isSupportedValue(value))
		{
			throw new IllegalArgumentException("cannot use unsupported value of type '" + value.getClass().getName() + "' for property '" + property.getName() + "'!");
		}

		this.properties.put(property, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Property<T> property)
	{
		Object result = this.properties.get(property);

		if (result == null && this.parent != null)
		{
			return this.parent.getProperty(property);
		}

		if (result == null) return null;

		if (!property.isSupportedValue(result))
		{
			throw new IllegalArgumentException("found unsupported value of type '" + result.getClass().getName() + "' for property '" + property.getName() + "'!");
		}

		return (T) result;
	}

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> Asset<T> getProperty(PropertyAsset<T> property)
	{
		return (Asset<T>) this.getProperty((Property<Asset>) property);
	}

	public boolean hasProperty(Property property)
	{
		return this.properties.containsKey(property) || (this.parent != null && this.parent.hasProperty(property));
	}

	public final Material getParent()
	{
		return this.parent;
	}
}
