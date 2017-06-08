package org.bstone.util.function;

import java.util.Objects;

/**
 * Created by Andy on 5/22/17.
 */

/**
 * Represents an operation that accepts two input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 * <p>
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the input to the operation
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Consumer2<T, V>
{

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the first input argument
	 * @param v the second input argument
	 */
	void accept(T t, V v);

	/**
	 * Returns a composed {@code Consumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation.  If performing this operation throws an exception,
	 * the {@code after} operation will not be performed.
	 *
	 * @param after the operation to perform after this operation
	 *
	 * @return a composed {@code Consumer} that performs in sequence this operation followed by the
	 * {@code after} operation
	 *
	 * @throws NullPointerException if {@code after} is null
	 */
	default Consumer2<T, V> andThen(Consumer2<? super T, ? super V> after)
	{
		Objects.requireNonNull(after);
		return (T t, V v) ->
		{
			accept(t, v);
			after.accept(t, v);
		};
	}
}