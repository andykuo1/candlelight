package apricot.bstone.mogli;

import apricot.bstone.RefCountSet;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import apricot.qsilver.poma.Poma;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 4/6/17.
 */
public final class Program implements AutoCloseable
{
	public static final Set<Program> PROGRAMS = new RefCountSet<>("Program");

	private Shader[] shaders;
	private int handle;

	private final Map<String, Integer> uniformLocations = new HashMap<>();

	public Program()
	{
		this.handle = GL20.glCreateProgram();

		if (this.handle == 0)
		{
			throw new GLException("Could not create shader program!");
		}

		PROGRAMS.add(this);
	}

	@Override
	public void close() throws Exception
	{
		GL20.glDeleteProgram(this.handle);
		this.handle = 0;

		PROGRAMS.remove(this);
	}

	public Program link(Shader... shaders)
	{
		this.shaders = shaders;
		for (int i = 0; i < this.shaders.length; ++i)
		{
			GL20.glAttachShader(this.handle, this.shaders[i].handle());
		}

		GL20.glLinkProgram(this.handle);

		int status = GL20.glGetProgrami(this.handle, GL20.GL_LINK_STATUS);
		if (status == GL11.GL_FALSE)
		{
			String infolog = GL20.glGetProgramInfoLog(this.handle);
			GL20.glDeleteProgram(this.handle);
			this.handle = 0;

			throw new GLException(infolog);
		}

		for (int i = 0; i < this.shaders.length; ++i)
		{
			GL20.glDetachShader(this.handle, this.shaders[i].handle());
		}

		return this;
	}

	public Program validate()
	{
		GL20.glValidateProgram(this.handle);

		int status = GL20.glGetProgrami(this.handle, GL20.GL_VALIDATE_STATUS);
		if (status == GL11.GL_FALSE)
		{
			String infoLog = GL20.glGetProgramInfoLog(this.handle);
			GL20.glDeleteProgram(this.handle);
			this.handle = 0;

			throw new GLException(infoLog);
		}

		return this;
	}

	public int handle()
	{
		return this.handle;
	}

	public void bind()
	{
		GL20.glUseProgram(this.handle);
	}

	public void unbind()
	{
		Poma.ASSERT(isInUse());
		GL20.glUseProgram(0);
	}

	public boolean isInUse()
	{
		return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM) == this.handle;
	}

	public int findUniformLocation(String uniformName)
	{
		if (uniformName == null)
		{
			throw new IllegalArgumentException("Missing uniform name!");
		}

		Integer location = this.uniformLocations.get(uniformName);
		if (location == null)
		{
			location = GL20.glGetUniformLocation(this.handle, uniformName);
			if (location < 0)
			{
				throw new GLException("Could not find uniform with name: " + uniformName);
			}

			this.uniformLocations.put(uniformName, location);
			return location;
		}

		return location;
	}

	public void setUniform(String uniformName, int x)
	{
		GL20.glUniform1i(this.findUniformLocation(uniformName), x);
	}

	public void setUniform(String uniformName, float x)
	{
		GL20.glUniform1f(this.findUniformLocation(uniformName), x);
	}

	public void setUniform(String uniformName, boolean x)
	{
		GL20.glUniform1i(this.findUniformLocation(uniformName), x ? 1 : 0);
	}

	public void setUniform(String uniformName, Vector2fc vec)
	{
		GL20.glUniform2f(this.findUniformLocation(uniformName), vec.x(), vec.y());
	}

	public void setUniform(String uniformName, Vector3fc vec)
	{
		GL20.glUniform3f(this.findUniformLocation(uniformName), vec.x(), vec.y(), vec.z());
	}

	public void setUniform(String uniformName, Vector4fc vec)
	{
		GL20.glUniform4f(this.findUniformLocation(uniformName), vec.x(), vec.y(), vec.z(), vec.w());
	}

	public void setUniform(String uniformName, Matrix4fc matrix)
	{
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			GL20.glUniformMatrix4fv(this.findUniformLocation(uniformName), false, fb);
		}
	}
}