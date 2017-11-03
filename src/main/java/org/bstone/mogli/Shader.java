package org.bstone.mogli;

import org.bstone.RefCountSet;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.qsilver.ResourceLocation;
import org.zilar.loader.StringLoader;

import java.util.Set;

/**
 * Created by Andy on 4/6/17.
 */
public final class Shader implements AutoCloseable
{
	public static final Set<Shader> SHADERS = new RefCountSet<>("Shader");

	private final int type;
	private int handle;

	public Shader(ResourceLocation location, int type)
	{
		this(StringLoader.read(location.getFilePath()), type);
	}

	public Shader(String source, int type)
	{
		this.type = type;

		this.handle = GL20.glCreateShader(this.type);
		if (this.handle == 0)
		{
			throw new GLException("Unable to create main.shader!");
		}

		GL20.glShaderSource(this.handle, source);
		GL20.glCompileShader(this.handle);

		int status = GL20.glGetShaderi(this.handle, GL20.GL_COMPILE_STATUS);
		if (status == GL11.GL_FALSE)
		{
			String infolog = GL20.glGetShaderInfoLog(this.handle);
			GL20.glDeleteShader(this.handle);
			this.handle = 0;

			throw new GLException(infolog);
		}

		SHADERS.add(this);
	}

	@Override
	public void close() throws Exception
	{
		GL20.glDeleteShader(this.handle);
		this.handle = 0;

		SHADERS.remove(this);
	}

	public int handle()
	{
		return this.handle;
	}

	public int type()
	{
		return this.type;
	}

	@Override
	public String toString()
	{
		return super.toString() + ":" + this.type + "@" + this.handle;
	}
}
