package canary.bstone.console.style;

import canary.bstone.console.ConsoleWriter;
import canary.bstone.console.action.ActionLink;

import java.awt.Color;
import java.util.function.Function;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Created by Andy on 12/3/17.
 */
public class StyleBuilder
{
	private final ConsoleWriter writer;
	private final StyleBuilder parent;

	private final SimpleAttributeSet attribs = new SimpleAttributeSet();
	private Function<Object, String> formatter = Object::toString;

	public StyleBuilder(ConsoleWriter writer)
	{
		this(writer, null);
	}

	public StyleBuilder(StyleBuilder styleBuilder)
	{
		this(styleBuilder.writer, styleBuilder);
	}

	public StyleBuilder(ConsoleWriter writer, StyleBuilder styleBuilder)
	{
		this.parent = styleBuilder;
		this.writer = writer;
	}

	public StyleBuilder setFormatter(Function<Object, String> formatter)
	{
		this.formatter = formatter;
		return this;
	}

	public StyleBuilder color(Color color)
	{
		StyleConstants.setForeground(this.attribs, color);
		return this;
	}

	public StyleBuilder underline(boolean underline)
	{
		StyleConstants.setUnderline(this.attribs, underline);
		return this;
	}

	public StyleBuilder action(ActionLink action)
	{
		this.attribs.addAttribute(ActionLink.KEY, action);
		return this;
	}

	public void formatln(Object msg)
	{
		this.format(msg);
		this.writer.newline();
	}

	public void format(Object msg)
	{
		if (msg != null)
		{
			this.writer.write(this.formatter.apply(msg), this.getAttributes());
		}
	}

	public javax.swing.text.AttributeSet getAttributes()
	{
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		if (this.parent != null)
		{
			attribs.addAttributes(this.parent.getAttributes());
		}
		attribs.addAttributes(this.attribs);
		return attribs;
	}
}
