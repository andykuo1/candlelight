package org.bstone.util.json;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONLiteral extends JSONValue
{
	protected final String value;

	JSONLiteral(String value)
	{
		this.value = value;
	}

	public String toLiteral()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		return this.value;
	}
}
