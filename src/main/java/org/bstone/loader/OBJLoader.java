package org.bstone.loader;

import net.jimboi.mod2.meshbuilder.ModelUtil;

import org.bstone.mogli.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJLoader
{
	public static Mesh read(String filepath)
	{
		FileReader fileReader = null;
		File objFile = new File(filepath);

		try
		{
			fileReader = new FileReader(objFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(fileReader);
		String line;
		List<Vertex> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		try
		{
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith("v "))
				{
					String[] currentLine = line.split(" ");
					Vector3f vertex = new Vector3f(
							Float.valueOf(currentLine[1]),
							Float.valueOf(currentLine[2]),
							Float.valueOf(currentLine[3]));
					Vertex newVertex = new Vertex(vertices.size(), vertex);
					vertices.add(newVertex);
				}
				else if (line.startsWith("vt "))
				{
					String[] currentLine = line.split(" ");
					Vector2f texture = new Vector2f(
							Float.valueOf(currentLine[1]),
							Float.valueOf(currentLine[2]));
					textures.add(texture);
				}
				else if (line.startsWith("vn "))
				{
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f(
							Float.valueOf(currentLine[1]),
							Float.valueOf(currentLine[2]),
							Float.valueOf(currentLine[3]));
					normals.add(normal);
				}
				else if (line.startsWith("f "))
				{
					break;
				}
			}

			while (line != null && line.startsWith("f "))
			{
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				_processVertices(vertex1, vertices, indices);
				_processVertices(vertex2, vertices, indices);
				_processVertices(vertex3, vertices, indices);
				line = reader.readLine();
			}

			reader.close();
		}
		catch (IOException e)
		{
			System.err.println("Error reading the file");
		}

		_removeUnusedVertices(vertices);

		float[] verticesArray = new float[vertices.size() * 3];
		float[] texturesArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		int[] indicesArray = new int[indices.size()];
		float furthestPoint = 0;

		for (int i = 0; i < vertices.size(); i++)
		{
			Vertex currentVertex = vertices.get(i);
			if (currentVertex.length > furthestPoint)
			{
				furthestPoint = currentVertex.length;
			}
			Vector3f position = currentVertex.position;
			Vector2f textureCoord = textures.get(currentVertex.textureIndex);
			Vector3f normalVector = normals.get(currentVertex.normalIndex);
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
		}

		for (int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}

		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, verticesArray, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, texturesArray, 1, 2, 0);
		ModelUtil.putVertexBuffer(mesh, normalsArray, 2, 3, 0);
		ModelUtil.putIndexBuffer(mesh, indicesArray);
		return mesh;
	}

	private static void _processVertices(String[] vertex, List<Vertex> vertices, List<Integer> indices)
	{
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;

		if (!currentVertex.isSet())
		{
			currentVertex.textureIndex = textureIndex;
			currentVertex.normalIndex = normalIndex;
			indices.add(index);
		}
		else
		{
			_processSetVertices(currentVertex, textureIndex, normalIndex, indices, vertices);
		}
	}

	private static void _processSetVertices(Vertex previousVertex, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<Vertex> vertices)
	{
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex))
		{
			indices.add(previousVertex.index);
		}
		else
		{
			Vertex anotherVertex = previousVertex.duplicateVertex;
			if (anotherVertex != null)
			{
				_processSetVertices(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
			}
			else
			{
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.position);
				duplicateVertex.textureIndex = newTextureIndex;
				duplicateVertex.normalIndex = newNormalIndex;
				previousVertex.duplicateVertex = duplicateVertex;
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.index);
			}
		}
	}

	private static void _removeUnusedVertices(List<Vertex> vertices)
	{
		for(Vertex vertex : vertices)
		{
			if(!vertex.isSet())
			{
				vertex.textureIndex = 0;
				vertex.normalIndex = 0;
			}
		}
	}

	private static final class Vertex
	{
		private static final int NO_INDEX = -1;

		public final Vector3f position;
		public final int index;
		public final float length;

		public int textureIndex = NO_INDEX;
		public int normalIndex = NO_INDEX;
		public Vertex duplicateVertex = null;

		public Vertex(int index, Vector3f position)
		{
			this.index = index;
			this.position = position;
			this.length = position.length();
		}

		public boolean isSet()
		{
			return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
		}

		public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther)
		{
			return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
		}
	}
}
