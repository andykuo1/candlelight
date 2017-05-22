package org.bstone.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts two arguments and produces a result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <T> the type of the first input to the function
 * @param <V> the type of the second input to the function
 * @param <R> the type of the result of the function
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Function2<T, V, R>
{
	/**
	 * Applies this function to the given argument.
	 *
	 * @param t the function argument
	 *
	 * @return the function result
	 */
	R apply(T t, V v);

	/**
	 * Returns a composed function that first applies the {@code before}
	 * function to its input, and then applies this function to the result.
	 * If evaluation of either function throws an exception, it is relayed to
	 * the caller of the composed function.
	 *
	 * @param <S>     the type of input to the {@code before} function, and to the composed
	 *                function
	 * @param before1 the function to apply before this function is applied
	 * @param before2 the function to apply before this function is applied
	 *
	 * @return a composed function that first applies the {@code before} function and then applies
	 * this function
	 *
	 * @throws NullPointerException if before is null
	 * @see #andThen(Function)
	 */
	default <S, P> Function2<S, P, R> compose(Function2<? super S, ? super P, ? extends T> before1, Function2<? super S, ? super P, ? extends V> before2)
	{
		Objects.requireNonNull(before1);
		Objects.requireNonNull(before2);
		return (S s, P p) -> apply(before1.apply(s, p), before2.apply(s, p));
	}

	/**
	 * Returns a composed function that first applies this function to
	 * its input, and then applies the {@code after} function to the result.
	 * If evaluation of either function throws an exception, it is relayed to
	 * the caller of the composed function.
	 *
	 * @param <S>   the type of output of the {@code after} function, and of the composed function
	 * @param after the function to apply after this function is applied
	 *
	 * @return a composed function that first applies this function and then applies the {@code
	 * after} function
	 *
	 * @throws NullPointerException if after is null
	 * @see #compose(Function2, Function2)
	 */
	default <S> Function2<T, V, S> andThen(Function<? super R, ? extends S> after)
	{
		Objects.requireNonNull(after);
		return (T t, V v) -> after.apply(apply(t, v));
	}
}
