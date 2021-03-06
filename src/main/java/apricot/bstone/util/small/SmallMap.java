package apricot.bstone.util.small;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SmallMap<Key, Value> extends AbstractMap<Key, Value>
{
	private ArrayList<Key> keys = null;
	private ArrayList<Value> values = null;

	public SmallMap()
	{
	}

	public SmallMap(Map<? extends Key, ? extends Value> map)
	{
		putAllInternal(this.keyArray(), this.valueArray(), map);
	}

	private static <Key, Value> void putAllInternal(ArrayList<Key> keys, ArrayList<Value> values, Map<? extends Key, ? extends Value> map)
	{
		for (Iterator<? extends Entry<? extends Key, ? extends Value>> i = map.entrySet().iterator(); i.hasNext(); )
		{
			Entry<? extends Key, ? extends Value> entry = i.next();
			putInternal(keys, values, entry.getKey(), entry.getValue());
		}
	}

	private static <Key, Value> Value putInternal(ArrayList<Key> keys, ArrayList<Value> values, Key key, Value value)
	{
		int i = keys.indexOf(key);
		if (i == -1)
		{
			keys.add(key);
			values.add(value);
			return null;
		} else
		{
			return values.set(i, value);
		}
	}

	private ArrayList<Key> keyArray()
	{
		if (this.keys == null)
		{
			this.keys = new ArrayList<Key>(0);
		}

		return this.keys;
	}

	private ArrayList<Value> valueArray()
	{
		if (this.values == null)
		{
			this.values = new ArrayList<Value>(0);
		}

		return this.values;
	}

	public Key keyAt(int index)
	{
		return this.keyArray().get(index);
	}

	public Value valueAt(int index)
	{
		return this.valueArray().get(index);
	}

	public int indexOf(Key key)
	{
		return this.keyArray().indexOf(key);
	}

	@Override
	public Value put(Key key, Value value)
	{
		return putInternal(this.keyArray(), this.valueArray(), key, value);
	}

	@Override
	public Set<Entry<Key, Value>> entrySet()
	{
		return new AbstractSet<Entry<Key, Value>>()
		{
			@Override
			public Iterator<Entry<Key, Value>> iterator()
			{
				return new Iterator<Entry<Key, Value>>()
				{

					private int posNext = 0;

					@Override
					public boolean hasNext()
					{
						return this.posNext < SmallMap.this.size();
					}

					@Override
					public Entry<Key, Value> next()
					{
						int pos = this.posNext++;
						return new SimpleEntry<Key, Value>(SmallMap.this.keys.get(pos), SmallMap.this.values.get(pos));
					}

					@Override
					public void remove()
					{
						int pos = --this.posNext;
						SmallMap.this.keys.remove(pos);
						SmallMap.this.values.remove(pos);
					}
				};
			}

			@Override
			public int size()
			{
				return (SmallMap.this.keys == null) ? 0 : SmallMap.this.keys.size();
			}
		};
	}

}