package org.bstone.material;

import org.bstone.asset.Asset;
import org.bstone.material.property.Property;
import org.bstone.material.property.PropertyAsset;
import org.bstone.mogli.Program;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andy on 11/2/17.
 */
public class CompiledMaterial implements AutoCloseable
{
	//TODO: This is the compiled solution for materials.
	//TODO: Instanced Materials should abide the properties of this
	private final Property[] properties;
	private final Object[] values;

	private final CompiledMaterial parent;

	public CompiledMaterial(Map<Property, Object> properties)
	{
		this(null, properties);
	}

	public CompiledMaterial(CompiledMaterial parent, Map<Property, Object> properties)
	{
		this.parent = parent;
		this.properties = new Property[properties.size()];
		this.values = new Object[this.properties.length];

		Iterator<Map.Entry<Property, Object>> iter = properties.entrySet().iterator();
		for(int i = 0; i < this.properties.length; ++i)
		{
			Map.Entry<Property, Object> entry = iter.next();
			this.properties[i] = entry.getKey();
			this.values[i] = entry.getValue();
		}
	}

	@Override
	public void close() throws Exception
	{
		for(int i = 0; i < this.properties.length; ++i)
		{
			this.properties[i] = null;
			this.values[i] = null;
		}
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

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Property<T> property)
	{
		for(int i = 0; i < this.properties.length; ++i)
		{
			if (this.properties[i] == property)
			{
				Object value = this.values[i];
				if (value == null) return null;

				if (!property.isSupportedValue(value))
				{
					throw new IllegalArgumentException("found unsupported value of type '" + value.getClass().getName() + "' for property '" + property.getName() + "'!");
				}

				return (T) value;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends AutoCloseable> Asset<T> getProperty(PropertyAsset<T> property)
	{
		return (Asset<T>) this.getProperty((Property<Asset>) property);
	}

	public boolean hasProperty(Property property)
	{
		for(int i = 0; i < this.properties.length; ++i)
		{
			if (this.properties[i] == property)
			{
				return true;
			}
		}

		return this.parent != null && this.parent.hasProperty(property);
	}

	public final CompiledMaterial getParent()
	{
		return this.parent;
	}
}
