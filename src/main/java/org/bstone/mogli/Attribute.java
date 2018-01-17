package org.bstone.mogli;

/**
 * Created by Andy on 1/17/18.
 */
public final class Attribute
{
	public final Program program;
	public final String name;
	public final int size;
	public final int type;
	public final int location;

	public Attribute(Program program, String name, int size, int type, int location)
	{
		this.program = program;
		this.name = name;
		this.size = size;
		this.type = type;
		this.location = location;
	}
}
