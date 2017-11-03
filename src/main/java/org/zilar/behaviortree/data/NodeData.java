package org.zilar.behaviortree.data;

import java.util.HashMap;
import java.util.Map;

public class NodeData
{
	private final String id;
	private final Map<String, Object> data = new HashMap<String, Object>();

	private NodeData superNode;

	protected NodeData(String id)
	{
		this.id = id;
	}

	/** Put all passed-in map entries into this cleared map */
	public final NodeData setData(Map<String, Object> map)
	{
		this.data.clear();
		this.data.putAll(map);
		return this;
	}

	protected final NodeData setSuperNode(NodeData node)
	{
		this.superNode = node;
		return this;
	}

	public final Object set(String key, Object value)
	{
		return this.data.put(key, value);
	}

	public final Object remove(String key)
	{
		return this.data.remove(key);
	}

	public final void clear()
	{
		this.data.clear();
	}

	/** Returns the value associated with key */
	public final Object get(String key)
	{
		return this.data.get(key);
	}

	/**
	 * Returns the value associated with key;
	 * <br>if value is null, then it will be replaced by passed-in defaultValue
	 */
	@SuppressWarnings("unchecked")
	public final <E extends Object> E get(String key, E defaultValue)
	{
		if (defaultValue == null)
		{
			throw new IllegalArgumentException();
		}

		Object value = this.data.get(key);
		if (value == null || !defaultValue.getClass().isAssignableFrom(value.getClass()))
		{
			this.data.put(key, defaultValue);
			return defaultValue;
		}

		return (E) value;
	}

	public final boolean contains(String key)
	{
		return this.data.containsKey(key);
	}

	public final NodeData getSuperNode()
	{
		return this.superNode;
	}

	public final String getID()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof NodeData)
		{
			return this.id.equals(((NodeData) o).id);
		}

		return false;
	}

	public final Map<String, Object> toMap()
	{
		return this.data;
	}
}
