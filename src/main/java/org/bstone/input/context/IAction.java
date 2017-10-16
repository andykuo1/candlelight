package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public interface IAction extends IState
{
	int getPreviousAction();
	int getAction();

	@Override
	default boolean isStateChanged()
	{
		return this.getPreviousAction() != this.getAction();
	}

	@Override
	default boolean getState()
	{
		return this.getAction() != 0;
	}
}
