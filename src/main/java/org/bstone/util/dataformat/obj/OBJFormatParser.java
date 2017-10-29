package org.bstone.util.dataformat.obj;

import org.bstone.mogli.Mesh;
import org.bstone.util.dataformat.DataFormatParser;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL15;
import org.zilar.meshbuilder.ModelUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/26/17.
 */
public class OBJFormatParser extends DataFormatParser<Mesh>
{
	public OBJFormatParser(int bufferSize)
	{
		super(bufferSize);
	}

	List<Vertex> vertices = new ArrayList<>();

	List<Vector4f> geometries = new ArrayList<>();

	List<Vector3f> textures = new ArrayList<>();
	List<Vector3f> normals = new ArrayList<>();
	List<Vector3f> params = new ArrayList<>();

	List<Integer> indices = new ArrayList<>();

	@Override
	protected Mesh readData(Cursor cursor) throws IOException
	{
		this.consumeWhiteSpaces(cursor);

		boolean vertexMode = true;
		while(!isEndOfFile(cursor.getChar()))
		{
			if (vertexMode)
			{
				if (this.readChar(cursor, '#'))
				{
					this.readComment(cursor);
				}
				if (this.readChar(cursor, 'v'))
				{
					this.readVertex(cursor);
				}
				else if (cursor.getChar() == 'f')
				{
					vertexMode = false;
				}
				else
				{
					//Simply ignore...
					this.consumeLine(cursor);
				}
			}
			else
			{
				if (this.readChar(cursor, 'f'))
				{
					List<Vector3i> face = this.readFace(cursor, new ArrayList<>());
					for(Vector3i vertex : face)
					{
						_processVertices(vertex, vertices, indices);
					}
				}
				else
				{
					break;
				}
			}

			this.consumeWhiteSpaces(cursor);
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
			Vector3f textureCoord = textures.get(currentVertex.textureIndex);
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
		ModelUtil.putVertexBuffer(mesh, verticesArray, GL15.GL_STATIC_DRAW, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, texturesArray, GL15.GL_STATIC_DRAW, 1, 2, 0);
		ModelUtil.putVertexBuffer(mesh, normalsArray, GL15.GL_STATIC_DRAW, 2, 3, 0);
		ModelUtil.putIndexBuffer(mesh, indicesArray, GL15.GL_STATIC_DRAW);
		return mesh;
	}

	protected void readVertex(Cursor cursor) throws IOException
	{
		if (this.readChar(cursor, ' '))
		{
			Vector4f vec = this.readVertexGeometry(cursor, new Vector4f());
			this.geometries.add(vec);

			//TODO: This needs to go and move to only geometries
			this.vertices.add(new Vertex(this.vertices.size(), new Vector3f(vec.x(), vec.y(), vec.z())));
		}
		else if (this.readChar(cursor, 't'))
		{
			Vector3f vec = this.readVertexTexture(cursor, new Vector3f());
			this.textures.add(vec);
		}
		else if (this.readChar(cursor, 'n'))
		{
			Vector3f vec = this.readVertexNormal(cursor, new Vector3f());
			this.normals.add(vec);
		}
		else if (this.readChar(cursor, 'p'))
		{
			Vector3f vec = this.readVertexParamSpace(cursor, new Vector3f());
			this.params.add(vec);
		}
		else
		{
			throw formatError(cursor, "unknown vertex type");
		}
	}

	protected Vector4f readVertexGeometry(Cursor cursor, Vector4f dst) throws IOException
	{
		this.consumeHorizontalWhiteSpaces(cursor);
		String x = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String y = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String z = this.readNumber(cursor);

		if (x.isEmpty() || y.isEmpty() || z.isEmpty())
		{
			throw formatError(cursor, "requires at least 3 components");
		}

		this.consumeHorizontalWhiteSpaces(cursor);
		String w = "1";
		if (!isVerticalWhiteSpace(cursor.getChar())) w = this.readNumber(cursor);

		return dst.set(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z), Float.valueOf(w));
	}

	protected Vector3f readVertexTexture(Cursor cursor, Vector3f dst) throws IOException
	{
		this.consumeHorizontalWhiteSpaces(cursor);
		String x = this.readNumber(cursor);
		if (x.isEmpty())
		{
			throw formatError(cursor, "requires at least 1 component");
		}

		this.consumeHorizontalWhiteSpaces(cursor);
		String y = "0";
		if (!isVerticalWhiteSpace(cursor.getChar())) y = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String z = "0";
		if (!isVerticalWhiteSpace(cursor.getChar())) z = this.readNumber(cursor);

		return dst.set(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z));
	}

	protected Vector3f readVertexNormal(Cursor cursor, Vector3f dst) throws IOException
	{
		this.consumeHorizontalWhiteSpaces(cursor);
		String x = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String y = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String z = this.readNumber(cursor);

		if (x.isEmpty() || y.isEmpty() || z.isEmpty())
		{
			throw formatError(cursor, "requires 3 components");
		}

		return dst.set(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z));
	}

	protected Vector3f readVertexParamSpace(Cursor cursor, Vector3f dst) throws IOException
	{
		this.consumeHorizontalWhiteSpaces(cursor);
		String x = this.readNumber(cursor);

		this.consumeHorizontalWhiteSpaces(cursor);
		String y = this.readNumber(cursor);

		if (x.isEmpty() || y.isEmpty())
		{
			throw formatError(cursor, "requires at least 2 components");
		}

		this.consumeHorizontalWhiteSpaces(cursor);
		String z = "1";
		if (!isVerticalWhiteSpace(cursor.getChar())) z = this.readNumber(cursor);

		return dst.set(Float.valueOf(x), Float.valueOf(y), Float.valueOf(z));
	}

	protected List<Vector3i> readFace(Cursor cursor, List<Vector3i> dst) throws IOException
	{
		int i = 0;
		this.consumeHorizontalWhiteSpaces(cursor);
		while(!isVerticalWhiteSpace(cursor.getChar()))
		{
			Vector3i vec = this.readFaceElement(cursor, new Vector3i());
			++i;
			dst.add(vec);
			this.consumeHorizontalWhiteSpaces(cursor);
		}

		if (i < 3)
		{
			throw formatError(cursor, "requires at least 3 vertices");
		}

		return dst;
	}

	protected Vector3i readFaceElement(Cursor cursor, Vector3i dst) throws IOException
	{
		String v = this.readInteger(cursor);
		String vt = "-1", vn = "-1";

		if (v.isEmpty())
		{
			throw formatError(cursor, "requires vertex index");
		}

		if (this.readChar(cursor, '/'))
		{
			if (cursor.getChar() != '/')
			{
				vt = this.readInteger(cursor);
			}
		}

		if (this.readChar(cursor, '/'))
		{
			if (cursor.getChar() != ' ')
			{
				vn = this.readInteger(cursor);
			}
		}

		return dst.set(Integer.parseInt(v), Integer.parseInt(vt), Integer.parseInt(vn));
	}

	protected void readComment(Cursor cursor) throws IOException
	{
		this.consumeLine(cursor);
	}

	private void _processVertices(Vector3i vertex, List<Vertex> vertices, List<Integer> indices)
	{
		int index = vertex.x() - 1;
		int textureIndex = vertex.y() - 1;
		int normalIndex = vertex.z() - 1;

		Vertex currentVertex = vertices.get(index);
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
