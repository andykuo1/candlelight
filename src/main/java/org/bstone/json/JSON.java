package org.bstone.json;

import org.bstone.util.dataformat.json.JSONFormatParser;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Andy on 6/9/17.
 */
public class JSON
{
	public static final JSONLiteral NULL = new JSONLiteral("null");

	public static final JSONBoolean TRUE = new JSONBoolean(true);
	public static final JSONBoolean FALSE = new JSONBoolean(false);

	private JSON()
	{
	}

	public static JSONObject object(JSONObject.Entry... entries)
	{
		JSONObject obj = new JSONObject();
		for (JSONObject.Entry entry : entries)
		{
			obj.put(entry);
		}
		return obj;
	}

	public static JSONArray array(JSONValue... values)
	{
		JSONArray arr = new JSONArray();
		for (JSONValue value : values)
		{
			arr.add(value);
		}
		return arr;
	}

	public static JSONObject.Entry entry(String name, JSONValue value)
	{
		return new JSONObject.Entry(name, value);
	}

	public static JSONNumber value(int value)
	{
		return new JSONNumber(Integer.toString(value, 10));
	}

	public static JSONNumber value(long value)
	{
		return new JSONNumber(Long.toString(value, 10));
	}

	public static JSONNumber value(float value)
	{
		return new JSONNumber(Float.toString(value));
	}

	public static JSONNumber value(double value)
	{
		return new JSONNumber(Double.toString(value));
	}

	public static JSONBoolean value(boolean value)
	{
		return value ? TRUE : FALSE;
	}

	public static JSONString value(String value)
	{
		return new JSONString(value);
	}

	public static JSONLiteral literal(String value)
	{
		return "null".equals(value) ? NULL : new JSONLiteral(value);
	}

	public static JSONNumber number(String string)
	{
		return new JSONNumber(string);
	}

	private static final int BUFFER_SIZE = 2048;
	private static JSONWriter jsonWriter;
	private static JSONFormatParser jsonReader;

	public static void write(Writer writer, JSONObject obj) throws IOException
	{
		if (jsonWriter == null)
		{
			jsonWriter = new JSONWriter();
		}

		jsonWriter.write(writer, obj, false);
	}

	public static JSONValue read(Reader reader) throws IOException
	{
		if (jsonReader == null)
		{
			jsonReader = new JSONFormatParser(2048);
		}

		return jsonReader.parse(reader);
	}
}
