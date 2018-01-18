package org.bstone.mogli;

import org.bstone.RefCountSet;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Andy on 4/26/17.
 */
public final class Mesh implements AutoCloseable
{
	public static final Set<Mesh> MESHES = new RefCountSet<>("Mesh");

	private int handle;
	private VBO[] vertexBuffers;
	private VBO indexBuffer;

	private int vertexCount;

	public Mesh()
	{
		this.vertexBuffers = new VBO[0];
		this.vertexCount = 0;

		this.handle = GL30.glGenVertexArrays();

		MESHES.add(this);
	}

	@Override
	public void close() throws Exception
	{
		for(int i = 0; i < this.vertexBuffers.length; ++i)
		{
			this.vertexBuffers[i].close();
			this.vertexBuffers[i] = null;
		}

		if (this.indexBuffer != null)
		{
			this.indexBuffer.close();
			this.indexBuffer = null;
		}

		GL30.glDeleteVertexArrays(this.handle);
		this.handle = 0;

		MESHES.remove(this);
	}

	public Mesh setElementArrayBuffer(VBO buffer)
	{
		if (buffer.getTarget() != GL15.GL_ELEMENT_ARRAY_BUFFER)
		{
			throw new IllegalArgumentException("buffer is not an element array");
		}

		GL30.glBindVertexArray(this.handle);
		buffer.bind();

		if (this.indexBuffer != null)
		{
			try
			{
				this.indexBuffer.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		this.indexBuffer = buffer;
		this.vertexCount = this.indexBuffer.getLength();

		//This is to alter the render state!
		//DO NOT buffer.unbind();
		GL30.glBindVertexArray(0);

		return this;
	}

	public Mesh setVertexArrayBuffer(int location, int size, int stride, VBO buffer)
	{
		GL30.glBindVertexArray(this.handle);
		buffer.bind();

		GL20.glVertexAttribPointer(location, size, buffer.getType(), buffer.isNormalized(), stride, 0);
		if (this.vertexBuffers.length <= location)
		{
			this.vertexBuffers = Arrays.copyOf(this.vertexBuffers, location + 1);
		}

		VBO vbo = this.vertexBuffers[location];
		if (vbo != null)
		{
			try
			{
				vbo.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.vertexBuffers[location] = buffer;

		buffer.unbind();
		GL30.glBindVertexArray(0);

		return this;
	}

	public int handle()
	{
		return this.handle;
	}

	public void bind()
	{
		GL30.glBindVertexArray(this.handle);

		for(int i = 0; i < this.vertexBuffers.length; ++i)
		{
			if (this.vertexBuffers[i] != null)
			{
				GL20.glEnableVertexAttribArray(i);
			}
		}
	}

	public void unbind()
	{
		for(int i = 0; i < this.vertexBuffers.length; ++i)
		{
			if (this.vertexBuffers[i] != null)
			{
				GL20.glDisableVertexAttribArray(i);
			}
		}

		GL30.glBindVertexArray(0);
	}

	public VBO getVertexBuffer(int location)
	{
		return this.vertexBuffers[location];
	}

	public VBO getIndexBuffer()
	{
		return this.indexBuffer;
	}

	public int getVertexCount()
	{
		return this.vertexCount;
	}
}
