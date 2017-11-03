package org.bstone.material.property;

import org.bstone.mogli.Program;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyVec4 extends Property<Vector4fc>
{
	public PropertyVec4(String name)
	{
		super(Vector4fc.class, name);
	}

	@Override
	public void apply(Program program, Vector4fc value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform4f(location, value.x(), value.y(), value.z(), value.w());
	}
}
