package org.zilar.in4;

import org.bstone.newinput.device.event.InputEvent;

/**
 * Created by Andy on 1/26/18.
 */
public interface InputAdapter
{
	//Returns true if will consume event, should also write intent to dst.
	//Otherwise, dst should not be altered!
	boolean processInput(String intent, InputEvent event, InputState dst);
}
