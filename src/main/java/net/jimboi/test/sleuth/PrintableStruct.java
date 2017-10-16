package net.jimboi.test.sleuth;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Andy on 9/23/17.
 */
public class PrintableStruct
{
	protected static final Object DIVIDER = new Object();

	public PrintableStruct()
	{
	}

	private static StringBuilder parseFieldToString(Object o, Field field, int depth, StringBuilder dst)
	{
		String name = field.getName();

		Object value;
		try
		{
			value = field.get(o);
		}
		catch (IllegalAccessException e)
		{
			value = null;
		}

		return parseObjectToString(name, value, depth, dst);
	}

	private static StringBuilder parseObjectToString(String name, Object value, int depth, StringBuilder dst)
	{
		if (value == DIVIDER)
		{
			dst.append("- . - . - . - . - . -");
			if (name != null) dst.append(name);
			dst.append('\n');
		}
		else
		{
			boolean dirty = false;
			if (depth >= 0)
			{
				StringBuilder prefix = new StringBuilder();
				for (int i = 0; i < depth; ++i) prefix.append("  ");
				dst.append(prefix.toString());

				dst.append((value instanceof Map ||
						value instanceof Iterable ||
						value instanceof PrintableStruct) ? " + " : " - ");
				if (name != null)
				{
					parseCamelCaseToString(name, dst);
					dst.append(": ");
				}

				dirty = true;
			}

			if (value instanceof Map)
			{
				if (dirty) dst.append('\n');

				Map map = (Map) value;
				for(Object key : map.keySet())
				{
					parseObjectToString(key.toString(), map.get(key), depth + 1, dst);
				}
			}
			else if (value instanceof Iterable)
			{
				if (dirty) dst.append('\n');

				for(Object object : (Iterable) value)
				{
					parseObjectToString(null, object, depth + 1, dst);
				}
			}
			else if (value instanceof PrintableStruct)
			{
				if (dirty) dst.append('\n');

				Field[] fields = value.getClass().getFields();
				for(Field field : fields)
				{
					parseFieldToString(value, field, depth + 1, dst);
				}
			}
			else
			{
				dst.append(value);
				dst.append('\n');
			}
		}

		return dst;
	}

	private static StringBuilder parseCamelCaseToString(String src, StringBuilder dst)
	{
		int startIndex = dst.length();

		dst.append(src);
		dst.setCharAt(startIndex, Character.toUpperCase(src.charAt(0)));

		for(int i = 1; i < src.length(); ++i)
		{
			if (Character.isUpperCase(src.charAt(i)))
			{
				dst.insert(startIndex + i, ' ');
				++i;
			}
		}

		return dst;
	}

	@Override
	public String toString()
	{
		return parseObjectToString(null, this, -1, new StringBuilder()).toString();
	}
}
