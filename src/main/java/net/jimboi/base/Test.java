package net.jimboi.base;

import org.bstone.util.dataformat.obj.OBJFormatParser;
import org.bstone.util.dataformat.obj.WavefrontOBJ;
import org.qsilver.poma.Poma;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Andy on 5/28/17.
 */
public class Test
{
	public static void main(String[] args)
	{
		Poma.makeSystemLogger();

		File file = new File("src/main/res/boo.obj");
		System.out.println(file.getAbsoluteFile());
		try
		{
			FileReader reader = new FileReader(file);
			OBJFormatParser parser = new OBJFormatParser(2048);
			WavefrontOBJ value = parser.parse(reader);
			System.out.println(value);
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
