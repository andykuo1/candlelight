package net.jimboi.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/28/17.
 */
public class MeshBoxBuilder extends MeshFaceBuilder
{
	private List<VertexBox> boxes = new ArrayList<>();

	public void addBox(VertexBox box)
	{
		this.boxes.add(box);
	}

	public List<VertexFace> getFaces()
	{
		List<VertexFace> faces = new ArrayList<>();
		for(VertexBox box : this.boxes)
		{
			faces.addAll(box.getFaces());
		}
		return faces;
	}

	public List<Vertex> getVertices()
	{
		List<Vertex> vertices = new ArrayList<>();
		vertices.addAll(super.getVertices());
		for(VertexBox box : this.boxes)
		{
			vertices.addAll(box.getVertices());
		}
		return vertices;
	}
}
