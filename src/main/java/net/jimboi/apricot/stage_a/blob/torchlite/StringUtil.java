package net.jimboi.apricot.stage_a.blob.torchlite;

import java.util.ArrayList;

public class StringUtil
{
	public static boolean isOnlyNumber(String string)
	{
		for(int i = 0; i < string.length(); ++i)
		{
			if (!Character.isDigit(string.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static String[] split(String parString, String parSeparator)
	{
		if (!parString.contains(parSeparator)) return new String[] {parString};

		ArrayList<String> result = new ArrayList<String>();

		while(parString.contains(parSeparator))
		{
			int i = parString.indexOf(parSeparator);
			result.add(parString.substring(0, i));
			parString = parString.substring(i + 1, parString.length());
		}
		result.add(parString);

		return result.toArray(new String[result.size()]);
	}

	public static String repeat(String str, int iterations)
	{
		StringBuilder sb = new StringBuilder();
		for(; iterations >= 0; --iterations)
		{
			sb.append(str);
		}
		return sb.toString();
	}
}
