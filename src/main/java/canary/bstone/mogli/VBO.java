package canary.bstone.mogli;

import canary.bstone.RefCountSet;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Set;

/**
 * Created by Andy on 4/26/17.
 */
public final class VBO implements AutoCloseable
{
	public static final Set<VBO> VBOS = new RefCountSet<>("VBO");

	private int handle;
	private int target;

	private int type;
	private boolean normalized;
	private int length;

	public VBO(int target)
	{
		this.target = target;

		this.handle = GL15.glGenBuffers();
		this.normalized = false;
		this.length = 0;

		VBOS.add(this);
	}

	public VBO()
	{
		this(GL15.GL_ARRAY_BUFFER);
	}

	@Override
	public void close() throws Exception
	{
		GL15.glDeleteBuffers(this.handle);
		this.handle = 0;

		VBOS.remove(this);
	}

	public VBO putData(IntBuffer buffer, boolean normalized, int usage)
	{
		GL15.glBindBuffer(this.target, this.handle);
		GL15.glBufferData(this.target, buffer, usage);
		this.type = GL11.GL_UNSIGNED_INT;
		this.normalized = normalized;
		this.length = buffer.limit();

		return this;
	}

	public VBO putData(FloatBuffer buffer, boolean normalized, int usage)
	{
		GL15.glBindBuffer(this.target, this.handle);
		GL15.glBufferData(this.target, buffer, usage);
		this.type = GL11.GL_FLOAT;
		this.normalized = normalized;
		this.length = buffer.limit();

		return this;
	}

	public VBO updateData(IntBuffer buffer, long offset)
	{
		if (this.type != GL11.GL_UNSIGNED_INT)
		{
			throw new IllegalArgumentException("Cannot store mismatching type in buffer!");
		}

		if (offset + buffer.limit() > this.length)
		{
			int usage = GL15.glGetBufferParameteri(this.target, GL15.GL_BUFFER_USAGE);
			this.putData(buffer, this.normalized, usage);
		}

		GL15.glBindBuffer(this.target, this.handle);
		GL15.glBufferSubData(this.target, offset, buffer);

		return this;
	}

	public VBO updateData(FloatBuffer buffer, long offset)
	{
		if (this.type != GL11.GL_FLOAT)
		{
			throw new IllegalArgumentException("Cannot store mismatching type in buffer!");
		}

		if (offset + buffer.limit() > this.length)
		{
			int usage = GL15.glGetBufferParameteri(this.target, GL15.GL_BUFFER_USAGE);
			this.putData(buffer, this.normalized, usage);
		}

		GL15.glBindBuffer(this.target, this.handle);
		GL15.glBufferSubData(this.target, offset, buffer);

		return this;
	}

	public int handle()
	{
		return this.handle;
	}

	public void bind()
	{
		GL15.glBindBuffer(this.target, this.handle);
	}

	public void unbind()
	{
		GL15.glBindBuffer(this.target, 0);
	}

	public boolean isNormalized()
	{
		return this.normalized;
	}

	public int getType() {
		return this.type;
	}

	public int getLength()
	{
		return this.length;
	}

	public int getTarget()
	{
		return this.target;
	}
}
