package net.jimboi.test.console;

import org.bstone.util.Procedure;

/**
 * Created by Andy on 8/29/17.
 */
public class ConsoleHelper
{
	private static String DIVIDER = "-=";
	private static String DIVIDER_SOFT = "- ";
	private static String DIVIDER_THIN = "==";
	private static String DIVIDER_FULL = "==";
	private static int LENGTH = 33;
	static
	{
		DIVIDER = expand(DIVIDER, LENGTH);
		DIVIDER_SOFT = expand(DIVIDER_SOFT, LENGTH);
		DIVIDER_THIN = expand(DIVIDER_THIN, LENGTH);
		DIVIDER_FULL = expand(DIVIDER_FULL, LENGTH);
	}

	private static String expand(String src, int length)
	{
		final StringBuilder sb = new StringBuilder();
		int j = 0;
		for(int i = length - 1; i >= 0; --i, ++j)
		{
			if (j >= src.length()) j = 0;
			sb.append(src.charAt(j));
		}
		return sb.toString();
	}

	public static void title(Console console, String title)
	{
		console.println(DIVIDER);
		console.print(" ");
		console.println(title);
		console.println(DIVIDER);
		console.println();
	}

	public static void button(Console console, String label, Procedure handler)
	{
		console.print(" > ");
		console.beginAttributes().setActionLink(con -> handler.apply()).printEnd(label);
		console.println();
	}

	public static void message(Console console, String msg)
	{
		console.println(msg);
	}

	public static void newline(Console console)
	{
		console.println();
	}

	public static void delay(Console console, int time)
	{
		try
		{
			Thread.sleep(time * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void divider(Console console, boolean softline)
	{
		if (softline)
		{
			console.println(DIVIDER_SOFT);
		}
		else
		{
			console.println(DIVIDER);
		}
	}
}
