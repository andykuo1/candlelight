package org.bstone.util.json;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONBoolean extends JSONLiteral
{
	JSONBoolean(boolean value)
	{
		super(Boolean.toString(value));
	}

	public boolean toBoolean()
	{
		return Boolean.parseBoolean(this.value);
	}
}
