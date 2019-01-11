package apricot.bstone.json;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONString extends JSONValue
{
	protected final String value;

	JSONString(String value)
	{
		this.value = value;
	}

	public String get()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		return "\'" + this.value + "\'";
	}
}
