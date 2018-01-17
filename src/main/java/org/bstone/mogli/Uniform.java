package org.bstone.mogli;

/**
 * Created by Andy on 1/17/18.
 */
public final class Uniform
{
	public final Program program;
	public final String name;
	public final int type;
	public final int location;

	public Uniform(Program program, String name, int type, int location)
	{
		this.program = program;
		this.name = name;
		this.type = type;
		this.location = location;
	}
}
