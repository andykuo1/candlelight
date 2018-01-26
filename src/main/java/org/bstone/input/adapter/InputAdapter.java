package org.bstone.input.adapter;

import org.bstone.input.InputState;
import org.bstone.input.device.event.InputEvent;

/**
 * Created by Andy on 1/26/18.
 */
public interface InputAdapter
{
	//Returns true if will consume event, should also write intent to dst.
	//Otherwise, dst should not be altered!
	boolean processInput(String intent, InputEvent event, InputState dst);

	default void poll(String intent, InputState dst) {}
}
