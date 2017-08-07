package org.bstone.util.dataformat.obj;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 6/11/17.
 */
final class VertexBuffer
{
	protected static final int MAX_VERTEX_SIZE = 4;

	public enum Vertex
	{
		GEOMETRY(3, 4),
		TEXTURE(1, 3),
		NORMAL(3, 3),
		PARAMSPACE(1, 3);

		int minSize;
		int maxSize;

		Vertex(int minSize, int maxSize)
		{
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public int getMaxVertexSize()
		{
			return this.maxSize;
		}

		public int getMinVertexSize()
		{
			return this.minSize;
		}
	}

	private final List<Float> geometry;
	private final List<Float> texture;
	private final List<Float> normal;
	private final List<Float> paramSpace;

	private final int[] vertexSize;

	private final FloatBuffer buffer;

	public VertexBuffer()
	{
		this.geometry = new ArrayList<>();
		this.texture = new ArrayList<>();
		this.normal = new ArrayList<>();
		this.paramSpace = new ArrayList<>();

		this.vertexSize = new int[Vertex.values().length];

		this.buffer = FloatBuffer.allocate(MAX_VERTEX_SIZE);
	}

	public void pushBuffer(Vertex vertex)
	{
		List<Float> dst = this.getVertexList(vertex);

		this.buffer.flip();

		int limit = this.buffer.limit();
		if (limit > vertex.getMaxVertexSize())
			throw new IllegalArgumentException("Exceeded maximum vertex size for '" + vertex + "'!");
		if (limit < vertex.getMinVertexSize())
			throw new IllegalArgumentException("Exceeded minimum vertex size for '" + vertex + "'!");

		int vertexSize = this.vertexSize[vertex.ordinal()];
		if (vertexSize == 0)
		{
			this.vertexSize[vertex.ordinal()] = limit;
		}
		else if (vertexSize != limit)
		{
			throw new IllegalArgumentException("Failed to add vertex with size " + limit + ", expected vertex length " + vertexSize + "!");
		}

		while (this.buffer.hasRemaining())
		{
			dst.add(this.buffer.get());
		}
		this.buffer.clear();
	}

	public FloatBuffer getBuffer()
	{
		return this.buffer;
	}

	public int getVertexSize(Vertex vertex)
	{
		return this.vertexSize[vertex.ordinal()];
	}

	public List<Float> getVertexList(Vertex vertex)
	{
		switch (vertex)
		{
			case GEOMETRY:
				return this.geometry;
			case TEXTURE:
				return this.texture;
			case NORMAL:
				return this.normal;
			case PARAMSPACE:
				return this.paramSpace;
			default:
				throw new UnsupportedOperationException("Unable to recognize vertex type '" + vertex + "'!");
		}
	}
}