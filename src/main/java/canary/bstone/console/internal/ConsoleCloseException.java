package canary.bstone.console.internal;

/**
 * Created by Andy on 12/6/17.
 */
public class ConsoleCloseException extends RuntimeException
{
	public ConsoleCloseException()
	{
		super();
	}

	public ConsoleCloseException(String message)
	{
		super(message);
	}

	public ConsoleCloseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConsoleCloseException(Throwable cause)
	{
		super(cause);
	}

	protected ConsoleCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
