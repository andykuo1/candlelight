package boron.bstone.json;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONNumber extends JSONValue
{
	protected final String string;

	JSONNumber(String string)
	{
		this.string = string;
	}

	public boolean isDecimal()
	{
		return this.string.indexOf('.') != -1 || this.string.indexOf('E') != -1 || this.string.indexOf('e') != -1;
	}

	public int toInt()
	{
		return Integer.parseInt(this.string, 10);
	}

	public long toLong()
	{
		return Long.parseLong(this.string, 10);
	}

	public float toFloat()
	{
		return Float.parseFloat(this.string);
	}

	public double toDouble()
	{
		return Double.parseDouble(this.string);
	}

	@Override
	public String toString()
	{
		return this.string;
	}
}
