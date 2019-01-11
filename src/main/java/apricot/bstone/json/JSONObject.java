package apricot.bstone.json;

import apricot.bstone.util.small.SmallMap;

import java.util.Set;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONObject extends JSONCollection
{
	protected final SmallMap<String, JSONValue> data = new SmallMap<>();

	JSONObject()
	{
	}

	public JSONObject put(Entry entry)
	{
		this.data.put(entry.name, entry.value);
		return this;
	}

	public JSONObject put(String name, JSONValue value)
	{
		this.data.put(name, value);
		return this;
	}

	public JSONObject putAll(JSONObject jsonObj)
	{
		this.data.putAll(jsonObj.data);
		return this;
	}

	public JSONObject remove(String name)
	{
		this.data.remove(name);
		return this;
	}

	public JSONValue get(String name)
	{
		return this.data.get(name);
	}

	public final Set<String> names()
	{
		return this.data.keySet();
	}

	public int size()
	{
		return this.data.size();
	}

	public boolean isEmpty()
	{
		return this.data.isEmpty();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		boolean first = true;
		for (String name : this.data.keySet())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(", ");
			}

			sb.append('\"');
			sb.append(name);
			sb.append('\"');
			sb.append(": ");
			sb.append(this.data.get(name));
		}
		sb.append(" }");
		return sb.toString();
	}

	public static final class Entry
	{
		private final String name;
		private final JSONValue value;

		public Entry(String name, JSONValue value)
		{
			this.name = name;
			this.value = value;
		}
	}
}
