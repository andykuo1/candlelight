package net.jimboi.test.console;

import org.bstone.util.Procedure;

import java.util.function.Consumer;

/**
 * Created by Andy on 8/29/17.
 */
public class ConsoleUtil
{
	private static int LENGTH = 39;

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
		divider(console, "-=");
		console.print(" ");
		console.println(title);
		divider(console, "-=");
		console.println();
	}

	public static void button(Console console, String label, Consumer<Console> handler)
	{
		button(console, label, () -> handler.accept(console));
	}

	public static void button(Console console, String label, Procedure handler)
	{
		console.print(" > ");
		console.beginAttributes().setActionLink(handler).printEnd(label);
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

	public static void divider(Console console, String token)
	{
		console.beginAttributes().printlnEnd(expand(token, LENGTH));
	}
}
