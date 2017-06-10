package org.bstone.util.json;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONFormatException extends IllegalArgumentException
{
	/**
	 * Constructs a <code>NumberFormatException</code> with no detail message.
	 */
	public JSONFormatException()
	{
		super();
	}

	/**
	 * Constructs a <code>NumberFormatException</code> with the
	 * specified detail message.
	 *
	 * @param s the detail message.
	 */
	public JSONFormatException(String s)
	{
		super(s);
	}

	/**
	 * Factory method for making a <code>NumberFormatException</code>
	 * given the specified input which caused the error.
	 *
	 * @param s the input causing the error
	 */
	static JSONFormatException forInputString(String s)
	{
		return new JSONFormatException("For input string: \"" + s + "\"");
	}
}