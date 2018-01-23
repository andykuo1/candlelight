package org.zilar.in.handler;

import org.zilar.in.InputState;

/**
 * Created by Andy on 1/22/18.
 */
public interface InputHandler
{
	boolean onAxisUpdate(InputState inputState, int axis, float oldValue, float newValue);
	boolean onButtonUpdate(InputState inputState, int button, int oldValue, int newValue);
}
