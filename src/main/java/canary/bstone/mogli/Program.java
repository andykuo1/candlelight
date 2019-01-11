package canary.bstone.mogli;

import canary.bstone.RefCountSet;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 4/6/17.
 */
public final class Program implements AutoCloseable
{
	public static final Set<Program> PROGRAMS = new RefCountSet<>("Program");

	public static Program createShaderProgram(String vertexSource, String fragmentSource)
	{
		Program program = new Program();
		try(Shader vertexShader = new Shader(vertexSource, GL20.GL_VERTEX_SHADER);
		    Shader fragmentShader = new Shader(fragmentSource, GL20.GL_FRAGMENT_SHADER))
		{
			program.link(vertexShader, fragmentShader);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return program;
	}

	private int handle;

	private Attribute[] attribs;
	private Map<String, Uniform> uniforms = new HashMap<>();

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
		for (int i = 0; i < shaders.length; ++i)
		{
			GL20.glAttachShader(this.handle, shaders[i].handle());
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

		for (int i = 0; i < shaders.length; ++i)
		{
			GL20.glDetachShader(this.handle, shaders[i].handle());
		}

		this.attribs = this.fetchAttribs();
		Uniform[] uniforms = this.fetchUniforms();
		for(Uniform uniform : uniforms)
		{
			this.uniforms.put(uniform.name, uniform);
		}

		return this;
	}

	private Attribute[] fetchAttribs()
	{
		int len = GL20.glGetProgrami(this.handle, GL20.GL_ACTIVE_ATTRIBUTES);
		int strlen = GL20.glGetProgrami(this.handle, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

		Attribute[] attribs = new Attribute[len];

		IntBuffer sizeBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer typeBuffer = BufferUtils.createIntBuffer(1);
		for(int i = 0; i < attribs.length; ++i)
		{
			String name = GL20.glGetActiveAttrib(this.handle, i, strlen, sizeBuffer, typeBuffer);
			int location = GL20.glGetAttribLocation(this.handle, name);
			attribs[i] = new Attribute(this, name, typeBuffer.get(), location);

			typeBuffer.flip();
		}

		return attribs;
	}

	private Uniform[] fetchUniforms()
	{
		int len = GL20.glGetProgrami(this.handle, GL20.GL_ACTIVE_UNIFORMS);
		int strlen = GL20.glGetProgrami(this.handle, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);

		Uniform[] uniforms = new Uniform[len];

		IntBuffer sizeBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer typeBuffer = BufferUtils.createIntBuffer(1);
		for(int i = 0; i < uniforms.length; ++i)
		{
			String name = GL20.glGetActiveUniform(this.handle, i, strlen, sizeBuffer, typeBuffer);
			int location = GL20.glGetUniformLocation(this.handle, name);
			uniforms[i] = new Uniform(this, name, typeBuffer.get(), location);

			typeBuffer.flip();
		}

		return uniforms;
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
		GL20.glUseProgram(0);
	}

	public boolean isInUse()
	{
		return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM) == this.handle;
	}

	public Attribute getAttribute(String name)
	{
		for(Attribute attrib : this.attribs)
		{
			if (attrib.name.equals(name))
			{
				return attrib;
			}
		}

		return null;
	}

	public Uniform getUniform(String name)
	{
		Uniform uniform = this.uniforms.get(name);
		if (uniform == null)
		{
			throw new GLException("Could not find uniform with name: " + name);
		}
		return uniform;
	}

	public int findUniformLocation(String uniformName)
	{
		Uniform uniform = this.uniforms.get(uniformName);
		if (uniform == null)
		{
			int location = GL20.glGetUniformLocation(this.handle, uniformName);
			if (location < 0)
			{
				throw new GLException("Could not find uniform with name: " + uniformName);
			}
			return location;
		}
		return uniform.location;
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