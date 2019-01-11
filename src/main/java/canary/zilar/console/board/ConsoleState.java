package canary.zilar.console.board;

import canary.zilar.console.Console;

/**
 * Created by Andy on 10/7/17.
 */
@FunctionalInterface
public interface ConsoleState
{
	default boolean begin(Console con, ConsoleBoard board)
	{
		return true;
	}

	default void end(Console con)
	{

	}

	void render(Console con);
}
