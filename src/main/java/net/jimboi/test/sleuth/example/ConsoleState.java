package net.jimboi.test.sleuth.example;

import org.bstone.console.Console;

/**
 * Created by Andy on 10/7/17.
 */
@FunctionalInterface
public interface ConsoleState
{
	void render(Console console);
}
