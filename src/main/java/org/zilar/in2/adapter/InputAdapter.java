package org.zilar.in2.adapter;

import org.bstone.newinput.device.event.InputEvent;
import org.zilar.in2.InputState;

/**
 * Created by Andy on 1/24/18.
 */
public interface InputAdapter
{
	boolean processEvent(InputEvent event, InputState dst);
}
