package org.qsilver.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Andy on 4/30/17.
 */
public class StringLoader
{
	public static String read(String filepath)
	{
		StringBuilder result = new StringBuilder();
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader(filepath));
			String buffer;
			while ((buffer = reader.readLine()) != null)
			{
				result.append(buffer + '\n');
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result.toString();
	}
}
