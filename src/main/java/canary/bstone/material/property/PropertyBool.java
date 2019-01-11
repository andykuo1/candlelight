package canary.bstone.material.property;

import canary.bstone.mogli.Program;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyBool extends Property<Boolean>
{
	public PropertyBool(String name)
	{
		super(Boolean.class, name);
	}

	@Override
	public void apply(Program program, Boolean value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform1i(location, value ? 1 : 0);
	}
}
