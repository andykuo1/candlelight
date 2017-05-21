package net.jimboi.mesh;

import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 4/28/17.
 */
public class MeshBuilder
{
	private List<Vertex> vertices = new ArrayList<>();

	public void addVertex(Vertex vertex)
	{
		this.vertices.add(vertex);
	}

	public List<Vertex> getVertices()
	{
		return this.vertices;
	}

	public MeshData compile(List<Vertex> vertices)
	{
		List<Vector3fc> positions = new ArrayList<>();
		List<Vector3fc> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		List<List<List<Float>>> attributes = new ArrayList<>();

		int attribListLength = -1;
		for(int i = 0; i < vertices.size(); ++i)
		{
			Vertex vert = vertices.get(i);

			if (attribListLength == -1)
			{
				attribListLength = vert.attributes.length;
			}
			else if (attribListLength != vert.attributes.length)
			{
				throw new IllegalStateException("Invalid vertex attribute!");
			}

			int j = positions.indexOf(vert.position);
			if (j == -1)
			{
				j = positions.size();
				positions.add(vert.position);
				normals.add(vert.normal);

				List<List<Float>> attribList = new ArrayList<>();
				for(int k = 0; k < attribListLength; ++k)
				{
					List<Float> attrib = new ArrayList<>();
					float[] attribValue = vert.attributes[k];
					for(int m = 0; m < attribValue.length; ++m)
					{
						attrib.add(attribValue[m]);
					}
					attribList.add(attrib);
				}
				attributes.add(attribList);
			}
			indices.add(j);
		}

		float[] posarray = new float[positions.size()];
		int i = 0;
		for(Vector3fc vec : positions)
		{
			posarray[i++] = vec.x();
			posarray[i++] = vec.y();
			posarray[i++] = vec.z();
		}

		int[] indexarray = new int[indices.size()];
		i = 0;
		for(Integer index : indices)
		{
			indexarray[i++] = index;
		}

		float[] normalarray = new float[normals.size()];
		i = 0;
		for(Vector3fc vec : normals)
		{
			normalarray[i++] = vec.x();
			normalarray[i++] = vec.y();
			normalarray[i++] = vec.z();
		}

		float[][] attribarray = new float[attribListLength][];
		for(i = 0; i < attribListLength; ++i)
		{
			List<Float> attribValues = new ArrayList<>();
			for (List<List<Float>> attribList : attributes)
			{
				attribValues.addAll(attribList.get(i));
			}
			attribarray[i] = new float[attribValues.size()];
			for(int j = 0; j < attribValues.size(); ++j)
			{
				attribarray[i][j] = attribValues.get(j);
			}
		}

		return new MeshData(posarray, indexarray, normalarray, attribarray);
	}
}
