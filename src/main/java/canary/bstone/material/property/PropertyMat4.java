package canary.bstone.material.property;

import canary.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

/**
 * Created by Andy on 11/1/17.
 */
public class PropertyMat4 extends Property<Matrix4fc>
{
	public PropertyMat4(String name)
	{
		super(Matrix4fc.class, name);
	}

	@Override
	public void apply(Program program, Matrix4fc value)
	{
		int location = program.findUniformLocation(this.getName());
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			GL20.glUniformMatrix4fv(location, false, fb);
		}
	}
}
