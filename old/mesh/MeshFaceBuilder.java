package net.jimboi.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/28/17.
 */
public class MeshFaceBuilder extends MeshBuilder
{
	private List<VertexFace> faces = new ArrayList<>();

	public void addFace(VertexFace face)
	{
		this.faces.add(face);
	}

	public List<VertexFace> getFaces()
	{
		return this.faces;
	}

	public List<Vertex> getVertices()
	{
		List<Vertex> vertices = new ArrayList<>();
		vertices.addAll(super.getVertices());
		for(VertexFace face : this.faces)
		{
			vertices.addAll(face.getVertices());
		}
		return vertices;
	}
}
