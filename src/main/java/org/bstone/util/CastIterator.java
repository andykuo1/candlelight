package org.bstone.util;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by Andy on 5/10/17.
 */
public class CastIterator<E> implements Iterator<E>
{
	private final Iterator<? extends E> iterator;

	public CastIterator(Iterator<? extends E> iterator)
	{
		this.iterator = iterator;
	}

	@Override
	public void remove()
	{
		this.iterator.remove();
	}

	@Override
	public void forEachRemaining(Consumer action)
	{
		this.iterator.forEachRemaining(action);
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public E next()
	{
		return this.iterator.next();
	}
}
