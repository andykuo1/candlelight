package net.jimboi.test.gumshoe;

import org.zilar.console.Console;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Andy on 12/2/17.
 */
public class Body
{
	private final Map<String, Object> values = new HashMap<>();
	private final String name;

	public Body(String name)
	{
		this.name = name;
	}

	public void set(String id, Object value)
	{
		this.values.put(id, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String id)
	{
		return (T) this.values.get(id);
	}

	public <T> T getOrCompute(String id, Function<String, T> action)
	{
		if (!this.contains(id))
		{
			T t = action.apply(id);
			this.set(id, t);
			return t;
		}
		else
		{
			return this.get(id);
		}
	}

	public boolean contains(String id)
	{
		return this.values.containsKey(id);
	}

	@Override
	public String toString()
	{
		if (this.contains("name"))
		{
			return this.name + "." + this.get("name");
		}
		else
		{
			return this.name;
		}
	}

	public static void println(Console con, Body body)
	{
		con.print(body.toString() + ":");
		printMap(con, body.values, "    ");
	}

	private static void printObject(Console con, Object value, String tabs)
	{
		if (value instanceof Map)
		{
			printMap(con, (Map) value, tabs + tabs);
		}
		else if (value instanceof Iterable)
		{
			printIterable(con, (Iterable) value, tabs + tabs);
		}
		else if (value instanceof String)
		{
			con.println("\"" + value + "\"");
		}
		else if (value instanceof Body)
		{
			con.println("" + value, Color.BLUE);
		}
		else
		{
			con.println("" + value);
		}
	}

	private static void printIterable(Console con, Iterable<?> iterable, String tabs)
	{
		con.println();
		for(Object obj : iterable)
		{
			con.print(tabs);
			printObject(con, obj, tabs);
		}
	}

	private static void printMap(Console con, Map<?, ?> map, String tabs)
	{
		con.println();
		for(Map.Entry<?, ?> entry : map.entrySet())
		{
			con.print(tabs);
			con.print("" + entry.getKey());
			con.print(" = ");
			printObject(con, entry.getValue(), tabs);
		}
	}
}
