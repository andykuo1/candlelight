package org.bstone.material.property;

import org.bstone.mogli.Program;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyInt extends Property<Integer>
{
	public PropertyInt(String name)
	{
		super(Integer.class, name);
	}

	@Override
	public void apply(Program program, Integer value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform1i(location, value);
	}
}
