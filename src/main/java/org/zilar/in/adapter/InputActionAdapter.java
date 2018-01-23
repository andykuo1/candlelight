package org.zilar.in.adapter;

import org.zilar.in.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public abstract class InputActionAdapter extends InputAdapter
{
	public abstract Integer getAction(InputState inputState);
}
