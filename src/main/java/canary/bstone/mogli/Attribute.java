package canary.bstone.mogli;

/**
 * Created by Andy on 1/17/18.
 */
public final class Attribute
{
	public final Program program;
	public final String name;
	public final int type;
	public final int location;

	public Attribute(Program program, String name, int type, int location)
	{
		this.program = program;
		this.name = name;
		this.type = type;
		this.location = location;
	}
}
