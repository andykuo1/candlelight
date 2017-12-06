package org.bstone.console.program;

import org.bstone.console.Console;

/**
 * Created by Andy on 12/3/17.
 */
public interface ConsoleProgram
{
	default boolean begin(Console con, ConsoleBoard board)
	{
		return true;
	}

	default void end(Console con) {}

	void main(Console con);

	default boolean input(Console con, String input)
	{
		return false;
	}
}
