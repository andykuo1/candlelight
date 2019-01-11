package apricot.bstone.util.small;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SmallSet<Value> extends AbstractSet<Value>
{
	private ArrayList<Value> values = null;

	public SmallSet()
	{
		this.values = new ArrayList<Value>();
	}

	public Value get(int index)
	{
		return this.values.get(index);
	}

	public int indexOf(Value value)
	{
		return this.values.indexOf(value);
	}

	public boolean add(int index, Value value)
	{
		if (this.values.contains(value)) return false;
		this.values.add(index, value);
		return true;
	}

	@Override
	public Iterator<Value> iterator()
	{
		return new Iterator<Value>()
		{
			private int posNext = 0;

			@Override
			public boolean hasNext()
			{
				return this.posNext < SmallSet.this.size();
			}

			@Override
			public Value next()
			{
				int pos = this.posNext++;
				return SmallSet.this.values.get(pos);
			}

			@Override
			public void remove()
			{
				int pos = --this.posNext;
				SmallSet.this.values.remove(pos);
			}
		};
	}

	@Override
	public int size()
	{
		return (SmallSet.this.values == null) ? 0 : SmallSet.this.values.size();
	}

	@Override
	public boolean contains(Object value)
	{
		for (Value v : this.values)
		{
			if (value.equals(v)) return true;
		}

		return false;
	}

	@Override
	public boolean add(Value value)
	{
		if (this.values.contains(value)) return false;
		return this.values.add(value);
	}

	@Override
	public boolean remove(Object value)
	{
		return this.values.remove(value);
	}

	public ArrayList<Value> toArrayList()
	{
		return this.values;
	}
}