package org.bstone.util.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Andy on 6/7/17.
 */
public class FilterIterator<E> implements Iterator<E>
{
	private final Iterator<? extends E> iterator;
	private final Predicate<E> predicate;

	private E next = null;

	public FilterIterator(Iterator<? extends E> iterator, Predicate<E> predicate)
	{
		this.iterator = iterator;
		this.predicate = predicate;
	}

	@Override
	public void remove()
	{
		this.iterator.remove();
	}

	@Override
	public void forEachRemaining(Consumer<? super E> action)
	{
		Objects.requireNonNull(action);
		while (hasNext())
		{
			action.accept(next());
		}
	}

	@Override
	public boolean hasNext()
	{
		if (this.next == null)
		{
			while (this.iterator.hasNext())
			{
				this.next = this.iterator.next();
				if (this.predicate.test(this.next))
				{
					return true;
				}
				else
				{
					this.next = null;
				}
			}
		}

		return this.next != null;
	}

	@Override
	public E next()
	{
		if (this.next == null)
		{
			if (!this.hasNext())
			{
				throw new NoSuchElementException();
			}
		}

		E obj = this.next;
		this.next = null;
		return obj;
	}
}