package org.bstone.material.property;

import org.bstone.mogli.Program;
import org.joml.Vector3fc;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyVec3 extends Property<Vector3fc>
{
	public PropertyVec3(String name)
	{
		super(Vector3fc.class, name);
	}

	@Override
	public void apply(Program program, Vector3fc value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform3f(location, value.x(), value.y(), value.z());
	}
}
