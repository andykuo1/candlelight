package org.zilar.in.adapter;

import org.zilar.in.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public abstract class InputRangeAdapter extends InputAdapter
{
	public abstract Float getRange(InputState inputState);
}
