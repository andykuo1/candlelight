package org.zilar.console;

import java.util.function.Consumer;

/**
 * Created by Andy on 10/7/17.
 */
final class Event<E>
{
	private final Consumer<E> consumer;
	private final E argument;

	Event(Consumer<E> consumer, E argument)
	{
		this.consumer = consumer;
		this.argument = argument;
	}

	public void execute()
	{
		this.consumer.accept(this.argument);
	}
}
