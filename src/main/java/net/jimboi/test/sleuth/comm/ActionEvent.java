package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;

import java.util.Random;

/**
 * Created by Andy on 9/25/17.
 */
public abstract class ActionEvent
{
	public abstract boolean evaluate(Console out, Simon user, EvaluationSheet dst);

	public abstract void execute(Console out, Simon user, Random rand, Environment env);

	public abstract String getName();
}
