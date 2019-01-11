package canary.bstone.util.iterator;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Andy on 1/23/18.
 */
public class FunctionIterator<E, T> implements Iterator<E>
{
	private final Iterator<T> iterator;
	private final Function<T, E> function;

	public FunctionIterator(Iterator<T> iterator, Function<T, E> function)
	{
		this.iterator = iterator;
		this.function = function;
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public E next()
	{
		return this.function.apply(this.iterator.next());
	}

	@Override
	public void remove()
	{
		this.iterator.remove();
	}

	@Override
	public void forEachRemaining(Consumer<? super E> action)
	{
		this.iterator.forEachRemaining(t -> action.accept(this.function.apply(t)));
	}
}
