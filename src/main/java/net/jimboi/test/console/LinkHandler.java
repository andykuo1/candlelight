package net.jimboi.test.console;

import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

/**
 * Created by Andy on 10/7/17.
 */
@FunctionalInterface
public interface LinkHandler
{
	static LinkHandler getLinkHandler(javax.swing.text.AttributeSet attrib)
	{
		return (LinkHandler) attrib.getAttribute(TextAttribute.LINK_HANDLER_KEY);
	}

	void execute(Console console, StyledDocument document, String text,
	             int beginIndex, int endIndex, Element element);
}
