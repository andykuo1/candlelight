package org.bstone.render.material;

import org.bstone.util.small.SmallMap;

import java.util.Map;

/**
 * Created by Andy on 8/10/17.
 */
public class Material
{
	private Map<String, Object> values = new SmallMap<>();
	private Map<String, Class> types = new SmallMap<>();

	public <T> void addProperty(String name, Class<? extends T> type, T defaultValue)
	{
		this.addProperty(name, type);
		this.setProperty(name, defaultValue);
	}

	public <T> void addProperty(String name, Class<? extends T> type)
	{
		if (this.types.containsKey(name))
		{
			throw new IllegalArgumentException("Property with name '" + name + "' already exists!");
		}

		this.values.put(name, null);
		this.types.put(name, type);
	}

	public Object removeProperty(String name)
	{
		Object o = this.values.remove(name);
		this.types.remove(name);
		return o;
	}

	public boolean hasProperty(String name)
	{
		return this.types.containsKey(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Class<? super T> type, String name)
	{
		Class<?> valueType = this.types.get(name);
		if (!valueType.isAssignableFrom(type))
		{
			throw new IllegalArgumentException("Found invalid type for property with name '" + name + "' - Expected '" + valueType.getSimpleName() + "', but found " + type.getSimpleName() + "' instead!");
		}
		return (T) this.values.get(name);
	}

	public <T> void setProperty(String name, T value)
	{
		if (!this.types.containsKey(name))
		{
			throw new IllegalArgumentException("Cannot find property of type '" + value.getClass().getSimpleName() + "' with name '" + name + "'!");
		}

		Class<?> valueType = this.types.get(name);
		if (!valueType.isInstance(value))
		{
			throw new IllegalArgumentException("Found invalid type for property with name '" + name + "' - Expected '" + valueType.getSimpleName() + "', but found " + value.getClass().getSimpleName() + "' instead!");
		}

		this.values.put(name, value);
	}

	public Material derive(Material dst)
	{
		dst.types.putAll(this.types);
		dst.values.putAll(this.values);
		return dst;
	}

	public boolean isSubsetOf(Material material)
	{
		for(String key : material.types.keySet())
		{
			if (!this.types.containsKey(key))
			{
				return false;
			}
		}
		return true;
	}
}
