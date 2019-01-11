package boron.base.asset.assetloader;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 6/12/17.
 */
public class AssetConstants
{
	private static final Set<Class> SOURCES = new HashSet<>();

	public static void clear()
	{
		SOURCES.clear();
	}

	public static void registerClassFields(Class src)
	{
		if (SOURCES.contains(src))
			throw new IllegalArgumentException("Class '" + src.getSimpleName() + "' is already registered!");

		SOURCES.add(src);
	}

	public static void removeClassFields(Class src)
	{
		if (!SOURCES.contains(src))
			throw new IllegalArgumentException("Class '" + src.getSimpleName() + "' is not yet registered!");

		SOURCES.remove(src);
	}

	public static Class getClass(String className)
	{
		for (Class src : SOURCES)
		{
			if (src.getSimpleName().toLowerCase().equals(className.toLowerCase()))
			{
				return src;
			}
		}

		throw new IllegalArgumentException("Invalid class name - could not find any registered classes with name '" + className + "'!");
	}

	public static boolean contains(Class src)
	{
		return SOURCES.contains(src);
	}

	public static int getInteger(Class src, String fieldName)
	{
		validateClass(src);

		try
		{
			Field field = src.getField(fieldName);
			return field.getInt(null);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		throw new UnsupportedOperationException("Unable to find 'int' field '" + fieldName + "' in '" + src.getSimpleName() + "'!");
	}

	public static float getFloat(Class src, String fieldName)
	{
		validateClass(src);

		try
		{
			Field field = src.getField(fieldName);
			return field.getFloat(null);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		throw new UnsupportedOperationException("Unable to find 'float' field '" + fieldName + "' in '" + src.getSimpleName() + "'!");
	}

	public static boolean getBoolean(Class src, String fieldName)
	{
		validateClass(src);

		try
		{
			Field field = src.getField(fieldName);
			return field.getBoolean(null);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		throw new UnsupportedOperationException("Unable to find 'boolean' field '" + fieldName + "' in '" + src.getSimpleName() + "'!");
	}

	private static void validateClass(Class src)
	{
		if (!contains(src))
			throw new IllegalArgumentException("Invalid class '" + src.getSimpleName() + "' - the class must first be registered!");
	}
}
