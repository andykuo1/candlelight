package org.qsilver.loader;

import org.bstone.util.json.JSON;
import org.bstone.util.json.JSONValue;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONLoader
{
	public static JSONValue read(String filepath)
	{
		JSONValue result = null;
		FileReader reader;
		try
		{
			reader = new FileReader(filepath);
			result = JSON.read(reader);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
}
