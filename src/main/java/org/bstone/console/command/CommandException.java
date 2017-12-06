package org.bstone.console.command;

/**
 * Created by Andy on 12/6/17.
 */
public class CommandException extends RuntimeException
{
	public CommandException()
	{
		super();
	}

	public CommandException(String message)
	{
		super(message);
	}

	public CommandException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CommandException(Throwable cause)
	{
		super(cause);
	}
}
