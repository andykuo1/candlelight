package org.bstone.window.input;

/**
 * Created by Andy on 7/17/17.
 */
public interface InputLayer extends Comparable<InputLayer>
{
	//TODO: This means that input can only be referenced from here...
	void onInputUpdate(InputEngine inputEngine);

	@Override
	default int compareTo(InputLayer o)
	{
		return o.hashCode() - this.hashCode();
	}
}
