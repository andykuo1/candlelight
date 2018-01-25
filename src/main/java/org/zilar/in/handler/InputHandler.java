package org.zilar.in.handler;

import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/22/18.
 */
public interface InputHandler
{
	boolean onAxisUpdate(RawInputState providerState, int axis, float oldValue, float newValue);
	boolean onButtonUpdate(RawInputState providerState, int button, int oldValue, int newValue);
}
