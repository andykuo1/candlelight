package org.bstone.input;

/**
 * Created by Andy on 10/16/17.
 */
public interface InputListener
{
	default void onInputStart(InputEngine input, InputContext context) {}
	default void onInputStop(InputEngine input, InputContext context) {}
	void onInputUpdate(InputContext context);
}
