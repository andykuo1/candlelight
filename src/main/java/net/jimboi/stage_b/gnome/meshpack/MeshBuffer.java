package net.jimboi.stage_b.gnome.meshpack;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Andy on 6/12/17.
 */
public class MeshBuffer
{
	public enum DrawType
	{
		ELEMENT,
		ARRAY
	}

	protected final FloatBuffer positions;
	protected final FloatBuffer texcoords;
	protected final FloatBuffer normals;

	protected final IntBuffer indices;

	protected final VertexType positionType;
	protected final VertexType texcoordType;
	protected final VertexType normalType;

	private final float furthest;

	private final DrawType drawType;

	protected MeshBuffer(VertexType positionType, int positions,
	                     VertexType texcoordType, int texcoords,
	                     VertexType normalType, int normals,
	                     int indices, float furthest)
	{
		if (!positionType.isAtLeast(VertexType.XYZ))
			throw new UnsupportedOperationException("invalid position vertex size (must be 3 or 4)");
		if (texcoordType.equals(VertexType.XYZW))
			throw new UnsupportedOperationException("invalid texcoord vertex size (must be less than 4)");
		if (!normalType.equals(VertexType.XYZ))
			throw new UnsupportedOperationException("invalid normal vertex size (must be 3)!");

		this.positionType = positionType;
		this.texcoordType = texcoordType;
		this.normalType = normalType;

		if (positions % this.positionType.getSize() != 0)
			throw new IllegalArgumentException("position vertex size does not match vertex count!");
		if (texcoords % this.texcoordType.getSize() != 0)
			throw new IllegalArgumentException("texcoord vertex size does not match vertex count!");
		if (normals % this.normalType.getSize() != 0)
			throw new IllegalArgumentException("normal vertex size does not match vertex count!");

		this.positions = BufferUtils.createFloatBuffer(positions);
		this.texcoords = BufferUtils.createFloatBuffer(texcoords);
		this.normals = BufferUtils.createFloatBuffer(normals);

		this.indices = BufferUtils.createIntBuffer(indices);

		this.furthest = furthest;
		this.drawType = indices == 0 ? DrawType.ARRAY : DrawType.ELEMENT;
	}

	public FloatBuffer getPositions()
	{
		return this.positions;
	}

	public int getPositionVertexSize()
	{
		return this.positionType.getSize();
	}

	public FloatBuffer getTexcoords()
	{
		return this.texcoords;
	}

	public int getTexcoordVertexSize()
	{
		return this.texcoordType.getSize();
	}

	public FloatBuffer getNormals()
	{
		return this.normals;
	}

	public int getNormalVertexSize()
	{
		return this.normalType.getSize();
	}

	public IntBuffer getIndices()
	{
		return this.indices;
	}

	public float getFurthestDistance()
	{
		return this.furthest;
	}

	public DrawType getDrawType()
	{
		return this.drawType;
	}

	enum VertexType
	{
		X,
		XY,
		XYZ,
		XYZW;

		public int getSize()
		{
			switch (this)
			{
				case X:
					return 1;
				case XY:
					return 2;
				case XYZ:
					return 3;
				case XYZW:
					return 4;
				default:
					throw new UnsupportedOperationException("Invalid vertex type!");
			}
		}

		public boolean isAtLeast(VertexType leastType)
		{
			if (leastType == this) return true;

			switch (this)
			{
				case X:
					return false;
				case XY:
					return leastType == X;
				case XYZ:
					return leastType == X || leastType == XY;
				case XYZW:
					return true;
				default:
					throw new UnsupportedOperationException("Invalid vertex type!");
			}
		}
	}
}
