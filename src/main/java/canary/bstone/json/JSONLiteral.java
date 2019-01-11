package canary.bstone.json;

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

	public boolean isNull()
	{
		return "null".equals(this.value);
	}

	@Override
	public String toString()
	{
		return this.value;
	}
}
