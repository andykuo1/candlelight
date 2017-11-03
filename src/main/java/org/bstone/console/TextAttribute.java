package org.bstone.console;

import org.bstone.util.function.Procedure;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Created by Andy on 10/7/17.
 */
public final class TextAttribute extends SimpleAttributeSet
{
	static final String LINK_HANDLER_KEY = "linkhandler";

	private final Console console;

	TextAttribute(Console console)
	{
		this.console = console;

		StyleConstants.setLineSpacing(this, 16F);
	}

	public TextAttribute setUnderline(boolean underline)
	{
		StyleConstants.setUnderline(this, underline);
		return this;
	}

	public TextAttribute setColor(Color color)
	{
		StyleConstants.setForeground(this, color);
		return this;
	}

	public TextAttribute setActionLink(Procedure action)
	{
		this.addAttribute(LINK_HANDLER_KEY, (LinkHandler) (console, document, text, beginIndex, endIndex, element) -> action.apply());
		this.setColor(this.console.getLinkColor());
		this.setUnderline(true);
		return this;
	}

	public TextAttribute setCustomLink(LinkHandler handler)
	{
		this.addAttribute(LINK_HANDLER_KEY, handler);
		return this;
	}

	public TextAttribute setAttribute(String key, Object value)
	{
		this.addAttribute(key, value);
		return this;
	}

	public synchronized Console printEnd(String msg)
	{
		this.console.print(msg, this);
		return this.console;
	}

	public synchronized Console printlnEnd(String msg)
	{
		this.console.print(msg, this);
		this.console.println();
		return this.console;
	}
}
