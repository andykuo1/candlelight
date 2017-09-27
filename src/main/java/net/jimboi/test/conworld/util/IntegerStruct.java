package net.jimboi.test.conworld.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 8/30/17.
 */
public class IntegerStruct implements Iterable<String>
{
	private final Set<String> fields = new HashSet<>();

	public IntegerStruct()
	{
		Field[] fields = this.getClass().getFields();
		for(Field field : fields)
		{
			this.fields.add(field.getName());
		}
	}

	public final void set(String stat, int value)
	{
		try
		{
			Field field = this.getClass().getField(stat);
			field.set(this, value);
		}
		catch (IllegalAccessException | NoSuchFieldException e)
		{
			throw new UnsupportedOperationException();
		}
	}

	public final void add(String stat, int offset)
	{
		try
		{
			Field field = this.getClass().getField(stat);
			int value = (int) field.get(this);
			field.set(this, value + offset);
		}
		catch (IllegalAccessException | NoSuchFieldException e)
		{
			throw new UnsupportedOperationException();
		}
	}

	public final int get(String stat)
	{
		try
		{
			Field field = this.getClass().getField(stat);
			return (int) field.get(this);
		}
		catch (IllegalAccessException | NoSuchFieldException e)
		{
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public final Iterator<String> iterator()
	{
		return this.fields.iterator();
	}
}
