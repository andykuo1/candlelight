package net.jimboi.stage_b.gnome.meshbuilder;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/24/17.
 */
public class MeshBuilderBase
{
	protected class Vertex
	{
		public final Vector3f position;
		public final Vector2f texcoord;
		public final Vector3f normal;

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

	protected final List<Vertex> vertices = new ArrayList<>();
	protected final List<Integer> indices = new ArrayList<>();
	private boolean dirtyNormals = false;

	public MeshData bake(boolean invertedNormals, boolean centered)
	{
		if (!this.dirtyNormals)
		{
			calculateNormals(this.vertices, this.indices);
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
		for (Vertex vertex : this.vertices)
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
		this.vertices.add(new Vertex(new Vector3f(x, y, z), new Vector2f(u, v), new Vector3f(i, j, k)));
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

	public static void calculateNormals(List<Vertex> vertices, List<Integer> indices)
	{
		for (Vertex vertex : vertices)
		{
			vertex.normal.set(0);
		}

		Vector3f normal = new Vector3f();
		Vector3f vec1 = new Vector3f();
		Vector3f vec2 = new Vector3f();
		for (int i = 0; i < indices.size(); i += 3)
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

		for (Vertex vertex : vertices)
		{
			vertex.normal.normalize();
		}
	}
}
