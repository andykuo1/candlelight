package org.bstone.console.style;

import org.bstone.console.Console;
import org.bstone.util.function.Procedure;

import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * Created by Andy on 8/29/17.
 */
public class ConsoleStyle
{
	public static void setLightMode(Console con)
	{
		Color back = new Color(0xFFFFFF);
		Color back2 = new Color(0xEEEEEE);
		Color fore = new Color(0x000000);
		Color link = new Color(0x6673D2);
		Color error = new Color(0xCF3B40);

		setConsoleInputColor(con, fore, back2);
		setConsoleColor(con, fore, back);
		con.getLinkStyle().color(link);
		con.getErrorStyle().color(error);
	}

	public static void setDarkMode(Console con)
	{
		Color back = new Color(0x1F1F1F);
		Color back2 = new Color(0x292929);
		Color fore = new Color(0xFFFFFF);
		Color link = new Color(0x798FBA);
		Color error = new Color(0xB67070);

		setConsoleInputColor(con, fore, back2);
		setConsoleColor(con, fore, back);
		con.getLinkStyle().color(link);
		con.getErrorStyle().color(error);
	}

	public static void setConsoleInputColor(Console con, Color foreground, Color background)
	{
		JTextField field = con.input().getComponent();
		field.setBackground(background);
		field.setForeground(foreground);
		field.setCaretColor(foreground);
	}

	public static void setConsoleColor(Console con, Color foreground, Color background)
	{
		JTextPane pane = con.getComponent();
		pane.setBackground(background);
		pane.setForeground(foreground);
		pane.setCaretColor(foreground);
	}

	public static void title(Console con, String title)
	{
		double d = con.writer().getTypeDelay();
		con.writer().setTypeDelay(0);
		divider(con, "-=");
		con.print(" ");
		con.println(title);
		divider(con, "-=");
		con.writer().setTypeDelay(30);
		con.println(" ");
		con.writer().setTypeDelay(d);
	}

	public static void button(Console con, String label, Consumer<Console> handler)
	{
		button(con, label, () -> handler.accept(con));
	}

	public static void button(Console con, String label, Procedure handler)
	{
		con.print(" > ");
		con.link(label, e -> handler.apply());
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
		con.println(repeatUntil(token, con.writer().getMaxLineLength() - (token.length() % 2 == 0 ? 1 : 0)));
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
