package org.bstone.console;

import org.bstone.util.Procedure;

import java.util.function.Consumer;

/**
 * Created by Andy on 8/29/17.
 */
public class ConsoleUtil
{
	public static void title(Console con, String title)
	{
		divider(con, "-=");
		con.print(" ");
		con.println(title);
		divider(con, "-=");
		con.println();
	}

	public static void button(Console con, String label, Consumer<Console> handler)
	{
		button(con, label, () -> handler.accept(con));
	}

	public static void button(Console con, String label, Procedure handler)
	{
		con.print(" > ");
		con.beginAttributes().setActionLink(handler).printlnEnd(label);
		con.println();
	}

	public static void message(Console con, String msg)
	{
		con.println(msg);
	}

	public static void newline(Console con)
	{
		con.println();
	}

	public static void divider(Console con, String token)
	{
		con.beginAttributes().printlnEnd(repeatUntil(token, con.getMaxLength() - (token.length() % 2 == 0 ? 1 : 0)));
	}

	public static String paddingUntil(String src, String padding, int length)
	{
		length -= src.length();
		final StringBuilder sb = new StringBuilder(src);
		int j = 0;
		for(int i = length - 1; i >= 0; --i, ++j)
		{
			if (j >= padding.length()) j = 0;
			sb.append(padding.charAt(j));
		}
		return sb.toString();
	}

	public static String repeatUntil(String src, int length)
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
}
