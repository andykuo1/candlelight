package org.bstone.console.state;

import org.bstone.console.Console;

/**
 * Created by Andy on 12/3/17.
 */
public interface ConsoleState
{
	default boolean begin(Console con, ConsoleStateMachine board)
	{
		return true;
	}

	default void end(Console con) {}

	void main(Console con);
}
