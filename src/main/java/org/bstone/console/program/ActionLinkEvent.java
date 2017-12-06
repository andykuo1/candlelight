package org.bstone.console.program;

import org.bstone.console.Console;

import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

/**
 * Created by Andy on 12/3/17.
 */
public class ActionLinkEvent
{
	public Console console;
	public ActionLink action;
	public StyledDocument document;
	public String text;
	public int beginIndex;
	public int endIndex;
	public Element element;
}
