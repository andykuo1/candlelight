package apricot.bstone.mogli;

/**
 * Created by Andy on 4/6/17.
 */
public class GLException extends RuntimeException
{
	public GLException()
	{
		super();
	}

	public GLException(String message)
	{
		super(message);
	}

	public GLException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GLException(Throwable cause)
	{
		super(cause);
	}

	protected GLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
