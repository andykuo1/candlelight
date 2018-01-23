package org.zilar.in.handler;

import org.bstone.util.iterator.FunctionIterator;
import org.bstone.util.pair.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 1/23/18.
 */
public class HandlerList<T> implements Iterable<T>
{
	private final List<PriorityPair<T>> handlers = new LinkedList<>();

	public void add(int priority, T handler)
	{
		PriorityPair<T> p = new PriorityPair<>(priority, handler);
		this.handlers.remove(p);
		for(int i = 0; i < this.handlers.size(); ++i)
		{
			if (this.handlers.get(i).compareTo(p) <= 0)
			{
				this.handlers.add(i, p);
				return;
			}
		}
		this.handlers.add(p);
	}

	public void add(T handler)
	{
		int priority = 0;
		if (!this.handlers.isEmpty())
		{
			priority = this.handlers.get(this.handlers.size() - 1).getPriority() + 1;
		}
		this.add(priority, handler);
	}

	public void remove(T handler)
	{
		this.handlers.remove(new PriorityPair<>(0, handler));
	}

	public void clear()
	{
		this.handlers.clear();
	}

	public int size()
	{
		return this.handlers.size();
	}

	@Override
	public Iterator<T> iterator()
	{
		return new FunctionIterator<>(this.handlers.iterator(), PriorityPair::getValue);
	}

	private static final class PriorityPair<T> extends Pair<Integer, T> implements Comparable<PriorityPair<?>>
	{
		public PriorityPair(Integer priority, T value)
		{
			super(priority, value);
		}

		public int getPriority()
		{
			return this.fst;
		}

		public T getValue()
		{
			return this.snd;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o)
		{
			return o instanceof PriorityPair && this.snd.equals(((PriorityPair) o).snd);
		}

		@Override
		public int compareTo(PriorityPair<?> o)
		{
			int diff = this.fst - o.fst;
			if (diff == 0) return this.hashCode() - o.hashCode();
			return diff;
		}
	}
}
