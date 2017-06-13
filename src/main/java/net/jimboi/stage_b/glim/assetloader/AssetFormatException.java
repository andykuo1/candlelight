package net.jimboi.stage_b.glim.assetloader;

/**
 * Created by Andy on 6/12/17.
 */
public class AssetFormatException extends IllegalArgumentException
{
	/**
	 * Constructs a <code>AssetFormatException</code> with no detail message.
	 */
	public AssetFormatException()
	{
		super();
	}

	/**
	 * Constructs a <code>AssetFormatException</code> with the
	 * specified detail message.
	 *
	 * @param s the detail message.
	 */
	public AssetFormatException(String s)
	{
		super(s);
	}

	/**
	 * Factory method for making a <code>AssetFormatException</code>
	 * given the specified input which caused the error.
	 *
	 * @param s the input causing the error
	 */
	static AssetFormatException forInputString(String s)
	{
		return new AssetFormatException("For input string: \"" + s + "\"");
	}
}