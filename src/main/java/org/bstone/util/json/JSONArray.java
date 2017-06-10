package org.bstone.util.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONArray extends JSONCollection
{
	protected final List<JSONValue> values = new ArrayList<>();

	JSONArray()
	{
	}

	public JSONArray add(JSONValue value)
	{
		this.values.add(value);
		return this;
	}

	public JSONArray addAll(JSONArray array)
	{
		this.values.addAll(array.values);
		return this;
	}

	public JSONArray set(int index, JSONValue value)
	{
		this.values.set(index, value);
		return this;
	}

	public JSONArray remove(int index)
	{
		this.values.remove(index);
		return this;
	}

	public JSONValue get(int index)
	{
		return this.values.get(index);
	}

	public int size()
	{
		return this.values.size();
	}

	public boolean isEmpty()
	{
		return this.values.isEmpty();
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		boolean first = true;
		for (JSONValue value : this.values)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(", ");
			}
			sb.append(value);
		}
		sb.append(" ]");
		return sb.toString();
	}
}
