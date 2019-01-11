package canary.bstone.material.property;

import canary.bstone.mogli.Program;
import org.joml.Vector2fc;
import org.lwjgl.opengl.GL20;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyVec2 extends Property<Vector2fc>
{
	public PropertyVec2(String name)
	{
		super(Vector2fc.class, name);
	}

	@Override
	public void apply(Program program, Vector2fc value)
	{
		int location = program.findUniformLocation(this.getName());
		GL20.glUniform2f(location, value.x(), value.y());
	}
}
