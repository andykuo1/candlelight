package org.bstone.input.context.adapter;

/**
 * Created by Andy on 10/29/17.
 */
public interface InputAdapter<T extends Comparable<T>>
{
	T poll();

	static IState asState(IAction action)
	{
		return () -> {
			Integer i = action.poll();
			if (i != null)
			{
				return i > 0;
			}
			return null;
		};
	}

	static IAction asAction(IState state)
	{
		return () -> {
			Boolean b = state.poll();
			if (b != null)
			{
				return b ? 1 : 0;
			}
			return null;
		};
	}

	static IRange asPositiveRange(IAction action)
	{
		return () -> {
			Integer i = action.poll();
			if (i != null)
			{
				return (Float) (float) i;
			}
			return null;
		};
	}

	static IRange asPositiveRange(IState state)
	{
		return () -> {
			Boolean b = state.poll();
			if (b != null)
			{
				return b ? 1F : 0F;
			}
			return null;
		};
	}

	static IRange asNegativeRange(IAction action)
	{
		return () -> {
			Integer i = action.poll();
			if (i != null)
			{
				return (Float) (float) -i;
			}
			return null;
		};
	}

	static IRange asNegativeRange(IState state)
	{
		return () -> {
			Boolean b = state.poll();
			if (b != null)
			{
				return b ? -1F : 0F;
			}
			return null;
		};
	}

	static IRange asRange(IState positive, IState negative)
	{
		return () -> {
			Boolean p = positive.poll();
			Boolean n = negative.poll();
			float f = 0;
			if (p != null && p) f += 1;
			if (n != null && n) f -= 1;
			return f;
		};
	}
}
