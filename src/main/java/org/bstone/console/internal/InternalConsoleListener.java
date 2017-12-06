package org.bstone.console.internal;

/**
 * Created by Andy on 12/3/17.
 */
public interface InternalConsoleListener
{
	default void onWriterAppend(String text, javax.swing.text.AttributeSet attrib) {}
	default void onWriterNewLine() {}
	default void onWriterDelete(String text) {}
	default void onWriterDeleteLine(String text) {}

	default void onInputKeyEvent(int keycode) {}
	default void onInputExecuteEvent(String input) {}
}
