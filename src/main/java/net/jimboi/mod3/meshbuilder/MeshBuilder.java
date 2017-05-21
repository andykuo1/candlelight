package net.jimboi.mod3.meshbuilder;

import org.bstone.util.ArrayUtil;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/10/17.
 */
public class MeshBuilder
{
	private final List<Vertex> vertices = new ArrayList<>();
	private final List<Integer> indices = new ArrayList<>();
	private boolean dirtyNormals = false;

	public MeshBuilder()
	{
	}

	public MeshData bake(boolean invertedNormals, boolean centered)
	{
		if (!this.dirtyNormals)
		{
			bakeNormals(this.vertices, this.indices);
		}

		int size = this.vertices.size();
		float[] v = new float[size * 3];
		float[] t = new float[size * 2];
		float[] n = new float[size * 3];
		float furthestLengthSquared = 0F;

		//Find center
		Vector3f offset = new Vector3f();
		if (centered)
		{
			//TODO: get a proper implementation of this
			Vector3f min = new Vector3f();
			Vector3f max = new Vector3f();
			for (Vertex vertex : this.vertices)
			{
				Vector3f vec = vertex.position;
				if (vec.lengthSquared() > max.lengthSquared())
				{
					max.set(vec);
				}

				if (vec.lengthSquared() < min.lengthSquared())
				{
					min.set(vec);
				}
			}
			offset.set(max).sub(min).div(2F);
		}

		int i = 0;
		int j = 0;
		int k = 0;
		int m = invertedNormals ? -1 : 1;
		for(Vertex vertex : this.vertices)
		{
			v[j] = vertex.position.x() - offset.x();
			v[j + 1] = vertex.position.y() - offset.y();
			v[j + 2] = vertex.position.z() - offset.z();

			t[k] = vertex.texcoord.x();
			t[k + 1] = vertex.texcoord.y();

			n[j] = vertex.normal.x() * m;
			n[j + 1] = vertex.normal.y() * m;
			n[j + 2] = vertex.normal.z() * m;

			float f = vertex.position.lengthSquared();
			if (f > furthestLengthSquared)
			{
				furthestLengthSquared = f;
			}

			++i;
			j = i * 3;
			k = i * 2;
		}
		int[] indices = ArrayUtil.toIntArray(this.indices.iterator(), new int[this.indices.size()], 0);

		return new MeshData(v, t, n, indices, (float) Math.sqrt(furthestLengthSquared));
	}

	public void clear()
	{
		this.vertices.clear();
		this.indices.clear();
	}

	public void addPlane(Vector3fc from, Vector3fc to, Vector2fc texTopLeft, Vector2fc texBotRight)
	{
		int i = this.getVertexCount();
		int normal = 1;

		this.addVertex(from.x(), from.y(), to.z(), texBotRight.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(to.x(), from.y(), to.z(), texTopLeft.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(from.x(), to.y(), to.z(), texBotRight.x(), texBotRight.y(), 0, 0, normal);
		this.addVertex(to.x(), from.y(), to.z(), texTopLeft.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(to.x(), to.y(), to.z(), texTopLeft.x(), texBotRight.y(), 0, 0, normal);
		this.addVertex(from.x(), to.y(), to.z(), texBotRight.x(), texBotRight.y(), 0, 0, normal);

		this.addVertexIndex(i++, i++, i++);
		this.addVertexIndex(i++, i++, i++);
	}

	public void addBox(Vector3fc from, Vector3fc to, Vector2fc texTopLeft, Vector2fc texBotRight, boolean bottom, boolean top, boolean front, boolean back, boolean left, boolean right)
	{
		this.addTexturedBox(from, to,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				bottom, top, front, back, left, right);
	}

	public void addTexturedBox(Vector3fc from, Vector3fc to, Vector2fc texFrontFrom, Vector2fc texFrontTo, Vector2fc texBackFrom, Vector2fc texBackTo, Vector2fc texTopFrom, Vector2fc texTopTo, Vector2fc texBotFrom, Vector2fc texBotTo, Vector2fc texLeftFrom, Vector2fc texLeftTo, Vector2fc texRightFrom, Vector2fc texRightTo, boolean bottom, boolean top, boolean front, boolean back, boolean left, boolean right)
	{
		int i = this.getVertexCount();

		final int normal = 1;

		//Bottom
		if (bottom)
		{
			this.addVertex(from.x(), from.y(), from.z(), texBotFrom.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), from.z(), texBotTo.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(from.x(), from.y(), to.z(), texBotFrom.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), from.z(), texBotTo.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), to.z(), texBotTo.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertex(from.x(), from.y(), to.z(), texBotFrom.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Top
		if (top)
		{
			this.addVertex(from.x(), to.y(), from.z(), texTopFrom.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(from.x(), to.y(), to.z(), texTopFrom.x(), texTopTo.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), from.z(), texTopTo.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), from.z(), texTopTo.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(from.x(), to.y(), to.z(), texTopFrom.x(), texTopTo.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), to.z(), texTopTo.x(), texTopTo.y(), 0, normal, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Front
		if (front)
		{
			this.addVertex(from.x(), from.y(), to.z(), texFrontTo.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(to.x(), from.y(), to.z(), texFrontFrom.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(from.x(), to.y(), to.z(), texFrontTo.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertex(to.x(), from.y(), to.z(), texFrontFrom.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(to.x(), to.y(), to.z(), texFrontFrom.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertex(from.x(), to.y(), to.z(), texFrontTo.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Back
		if (back)
		{
			this.addVertex(from.x(), from.y(), from.z(), texBackFrom.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(from.x(), to.y(), from.z(), texBackFrom.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertex(to.x(), from.y(), from.z(), texBackTo.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(to.x(), from.y(), from.z(), texBackTo.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(from.x(), to.y(), from.z(), texBackFrom.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertex(to.x(), to.y(), from.z(), texBackTo.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Left
		if (left)
		{
			this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertex(from.x(), from.y(), from.z(), texLeftFrom.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), to.z(), texLeftTo.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Right
		if (right)
		{
			this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
			this.addVertex(to.x(), from.y(), from.z(), texRightTo.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), to.z(), texRightFrom.x(), texRightTo.y(), normal, 0, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}
	}

	public void addFaceVertical(Vector3fc bottomLeft, Vector3fc topRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(0F, 1F));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), bottomLeft.z()), new Vector2f(0F, 0F));
		this.addVertex(topRight, new Vector2f(1F, 0F));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), topRight.z()), new Vector2f(1F, 1F));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceVertical(Vector3fc bottomLeft, Vector3fc topRight, Vector2fc texCoordTopLeft, Vector2fc texCoordBottomRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(texCoordTopLeft.x(), texCoordBottomRight.y()));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), bottomLeft.z()), texCoordTopLeft);
		this.addVertex(topRight, new Vector2f(texCoordBottomRight.x(), texCoordTopLeft.y()));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), topRight.z()), texCoordBottomRight);

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceHorizontal(Vector3f bottomLeft, Vector3f topRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(0F, 0F));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), bottomLeft.z()), new Vector2f(1F, 0F));
		this.addVertex(topRight, new Vector2f(1F, 1F));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), topRight.z()), new Vector2f(0F, 1F));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceHorizontal(Vector3fc bottomLeft, Vector3fc topRight, Vector2fc texCoordTopLeft, Vector2fc texCoordBottomRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, texCoordTopLeft);
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), bottomLeft.z()), new Vector2f(texCoordBottomRight.x(), texCoordTopLeft.y()));
		this.addVertex(topRight, texCoordBottomRight);
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), topRight.z()), new Vector2f(texCoordTopLeft.x(), texCoordBottomRight.y()));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addVertex(Vector3fc position, Vector2fc texcoord)
	{
		this.vertices.add(new Vertex(new Vector3f(position), new Vector2f(texcoord), new Vector3f()));
	}

	public void addVertex(float x, float y, float z, float u, float v)
	{
		this.vertices.add(new Vertex(new Vector3f(x, y, z), new Vector2f(u, v)));
	}

	public void addVertex(float x, float y, float z, float u, float v, float i, float j, float k)
	{
		this.vertices.add(new Vertex(new Vector3f(x, y, z), new Vector2f(u, v), new Vector3f(i, j ,k)));
		this.dirtyNormals = true;
	}

	public void addVertexIndex(int i, int j, int k)
	{
		this.indices.add(i);
		this.indices.add(j);
		this.indices.add(k);
	}

	public int getVertexCount()
	{
		return this.vertices.size();
	}

	private static void bakeNormals(List<Vertex> vertices, List<Integer> indices)
	{
		for(Vertex vertex : vertices)
		{
			vertex.normal.set(0);
		}

		Vector3f normal = new Vector3f();
		Vector3f vec1 = new Vector3f();
		Vector3f vec2 = new Vector3f();
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vertex vertex = vertices.get(i0);
			Vertex verta = vertices.get(i1);
			Vertex vertb = vertices.get(i2);
			verta.position.sub(vertex.position, vec1);
			vertb.position.sub(vertex.position, vec2);
			vec1.cross(vec2, normal);

			vertex.normal.add(normal);
			verta.normal.add(normal);
			vertb.normal.add(normal);
		}

		for(Vertex vertex : vertices)
		{
			vertex.normal.normalize();
		}
	}

	class Vertex
	{
		private final Vector3f position;
		private final Vector2f texcoord;
		private final Vector3f normal;

		public Vertex(Vector3f position, Vector2f texcoord)
		{
			this.position = position;
			this.texcoord = texcoord;
			this.normal = new Vector3f();
		}

		public Vertex(Vector3f position, Vector2f texcoord, Vector3f normal)
		{
			this.position = position;
			this.texcoord = texcoord;
			this.normal = normal;
		}
	}
}
