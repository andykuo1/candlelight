package org.bstone.console.program;

/**
 * Created by Andy on 12/3/17.
 */
@FunctionalInterface
public interface ActionLink
{
	String KEY = "actionlink";

	static ActionLink getActionFromAttributes(javax.swing.text.AttributeSet attrib)
	{
		return (ActionLink) attrib.getAttribute(KEY);
	}

	void execute(ActionLinkEvent e);
}
