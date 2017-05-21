package net.jimboi.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/28/17.
 */
public class VertexFace
{
	public final Vertex topLeft;
	public final Vertex bottomLeft;
	public final Vertex bottomRight;
	public final Vertex topRight;

	public VertexFace(VertexFace face, boolean mirrored)
	{
		if (mirrored)
		{
			this.topLeft = face.bottomLeft;
			this.bottomLeft = face.topLeft;
			this.topRight = face.bottomRight;
			this.bottomRight = face.topRight;
		}
		else
		{
			this.topLeft = face.topLeft;
			this.bottomLeft = face.bottomLeft;
			this.bottomRight = face.bottomRight;
			this.topRight = face.topRight;
		}
	}

	public VertexFace(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight, boolean mirrored)
	{
		if (mirrored)
		{
			this.topLeft = bottomLeft;
			this.bottomLeft = topLeft;
			this.topRight = bottomRight;
			this.bottomRight = topRight;
		}
		else
		{
			this.topLeft = topLeft;
			this.bottomLeft = bottomLeft;
			this.bottomRight = bottomRight;
			this.topRight = topRight;
		}
	}

	public List<Vertex> getVertices()
	{
		List<Vertex> vertices = new ArrayList<>();
		vertices.add(this.topLeft);
		vertices.add(this.bottomLeft);
		vertices.add(this.topRight);

		vertices.add(this.bottomRight);
		vertices.add(this.topRight);
		vertices.add(this.bottomLeft);
		return vertices;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof VertexFace)
		{
			VertexFace face = (VertexFace) o;
			return this.topRight.equals(face.topRight) &&
					this.bottomLeft.equals(face.bottomLeft) &&
					this.bottomRight.equals(face.bottomRight) &&
					this.topLeft.equals(face.topLeft);
		}

		return false;
	}
}
