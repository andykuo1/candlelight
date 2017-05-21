package net.jimboi.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/28/17.
 */
public class VertexBox
{
	public final VertexFace front;
	public final VertexFace back;
	public final VertexFace left;
	public final VertexFace right;
	public final VertexFace bottom;
	public final VertexFace top;

	public VertexBox(VertexFace front, VertexFace back, VertexFace left, VertexFace right, VertexFace bottom, VertexFace top)
	{
		this.front = front;
		this.back = back;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}

	public List<VertexFace> getFaces()
	{
		List<VertexFace> faces = new ArrayList<>();
		faces.add(this.front);
		faces.add(this.top);
		faces.add(this.right);
		faces.add(this.left);
		faces.add(this.bottom);
		faces.add(this.back);
		return faces;
	}

	public List<Vertex> getVertices()
	{
		List<Vertex> vertices = new ArrayList<>();
		vertices.addAll(this.front.getVertices());
		vertices.addAll(this.top.getVertices());
		vertices.addAll(this.right.getVertices());
		vertices.addAll(this.left.getVertices());
		vertices.addAll(this.bottom.getVertices());
		vertices.addAll(this.back.getVertices());
		return vertices;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof VertexBox)
		{
			VertexBox other = (VertexBox) o;
			return this.front.equals(other.front) &&
					this.back.equals(other.back) &&
					this.left.equals(other.left) &&
					this.right.equals(other.right) &&
					this.bottom.equals(other.bottom) &&
					this.top.equals(other.top);
		}

		return false;
	}
}
