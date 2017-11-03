package org.bstone.material.property;

import org.bstone.mogli.Program;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyFloat extends Property<Float>
{
	public PropertyFloat(String name)
	{
		super(Float.class, name);
	}

	@Override
	public void apply(Program program, Float value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform1f(location, value);
	}
}
