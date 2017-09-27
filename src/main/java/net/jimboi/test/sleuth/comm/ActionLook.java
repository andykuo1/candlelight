package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;

import java.util.Random;

/**
 * Created by Andy on 9/25/17.
 */
public class ActionLook extends ActionEvent
{
	@Override
	public boolean evaluate(Console out, Simon user, EvaluationSheet dst)
	{
		return false;
	}

	@Override
	public void execute(Console out, Simon user, Random rand, Environment env)
	{

	}

	@Override
	public String getName()
	{
		return "Look Around";
	}
}
