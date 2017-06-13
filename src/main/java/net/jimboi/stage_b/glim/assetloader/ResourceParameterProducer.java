package net.jimboi.stage_b.glim.assetloader;

import net.jimboi.stage_b.gnome.asset.ResourceParameter;

/**
 * Created by Andy on 6/13/17.
 */
@FunctionalInterface
public interface ResourceParameterProducer<T>
{
	ResourceParameter<T> create(Class type, Object[] args);

	static void validateArgumentLength(Class type, Object[] args, int length)
	{
		if (args.length >= length) return;

		throw new IllegalArgumentException("Invalid number of arguments for asset type '" + type.getSimpleName() + "'. Must be at least " + length + "!");
	}

	static void validateArgument(Class type, Class argType, Object arg)
	{
		if (argType.isInstance(arg)) return;

		throw new IllegalArgumentException("Invalid argument for asset type '" + type.getSimpleName() + "': Expected '" + argType.getSimpleName() + "', but found '" + arg + "'!");
	}

	static void validateArgumentEquals(Class type, Object valid, Object arg)
	{
		if (valid.equals(arg)) return;

		throw new IllegalArgumentException("Invalid argument for asset type '" + type.getSimpleName() + "': Expected '" + valid + "', but found '" + arg + "'!");
	}
}
