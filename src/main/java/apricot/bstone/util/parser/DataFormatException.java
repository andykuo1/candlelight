package apricot.bstone.util.parser;

/**
 * Created by Andy on 6/9/17.
 */
public class DataFormatException extends IllegalArgumentException
{
	/**
	 * Constructs a <code>DataFormatException</code> with no detail message.
	 */
	public DataFormatException()
	{
		super();
	}

	/**
	 * Constructs a <code>DataFormatException</code> with the
	 * specified detail message.
	 *
	 * @param s the detail message.
	 */
	public DataFormatException(String s)
	{
		super(s);
	}

	/**
	 * Factory method for making a <code>DataFormatException</code>
	 * given the specified input which caused the error.
	 *
	 * @param s the input causing the error
	 */
	static DataFormatException forInputString(String s)
	{
		return new DataFormatException("For input string: \"" + s + "\"");
	}
}