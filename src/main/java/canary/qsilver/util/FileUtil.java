package canary.qsilver.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FileUtil
{
	public static final int BYTES_PER_READ = 256;
	public static final char SEPERATOR = '/';

	private FileUtil()
	{
	}

	public static String getParentDir(String filepath)
	{
		return filepath.substring(0, filepath.lastIndexOf(SEPERATOR));
	}

	public static String getFileNameWithoutExt(String filepath)
	{
		String file = getFileName(filepath);
		int i = file.lastIndexOf(".");
		if (i == -1) return file;
		return file.substring(0, i);
	}

	public static String getFileName(String filepath)
	{
		String[] path = getPath(filepath);
		return path[path.length - 1];
	}

	public static String[] getPath(String filepath)
	{
		return filepath.split("" + SEPERATOR);
	}

	public static boolean isFile(String filepath)
	{
		return !isDirectory(filepath);
	}

	public static boolean isDirectory(String filepath)
	{
		return getFileExt(filepath).equals("");
	}

	public static String getFileExt(String filepath)
	{
		String file = getFileName(filepath);
		int i = file.lastIndexOf('.');
		if (i == -1) return "";
		return file.substring(i + 1);
	}

	public static boolean exists(String filepath)
	{
		File f = new File(filepath);
		return f.exists();
	}

	public static BufferedImage loadImageAsBuffered(String filepath)
	{
		try
		{
			BufferedImage image = ImageIO.read(new FileInputStream(filepath));
			return image;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static List<String> loadFileAsList(String filepath)
	{
		List<String> result = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			String buffer = "";
			while ((buffer = reader.readLine()) != null)
			{
				result.add(buffer);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static ByteBuffer loadFileAsByteBuffer(String filename)
	{
		File file = new File(filename);
		ByteBuffer bb = ByteBuffer.allocate((int) file.length());
		try
		{
			FileInputStream fis = new FileInputStream(filename);

			int bytesRead = 0;
			byte[] buf = new byte[BYTES_PER_READ];

			while (bytesRead != -1)
			{
				bb.put(buf, 0, bytesRead);
				bytesRead = fis.read(buf);
			}

			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return bb;
	}
}
