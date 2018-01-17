package org.zilar.meshbuilder;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.VBO;
import org.bstone.transform.Transform;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;

/**
 * Created by Andy on 6/12/17.
 */
public class MeshBuilder2D
{
	public Mesh createQuad()
	{
		Mesh mesh = new Mesh();
		mesh.bind();
		{
			VBO vbo = new VBO();
			vbo.bind();
			{
				final float[] verts = {-0.5F, 0.5F, -0.5F, -0.5F, 0.5F, -0.5F, 05.F, 05.F};
				vbo.putData(FloatBuffer.wrap(verts), false, GL15.GL_STATIC_DRAW);
			}
			vbo.unbind();
			mesh.setVertexArrayBuffer(0, 2, 0, vbo);
		}
		mesh.unbind();
		return mesh;
	}

	public Mesh createCircle(int segments)
	{
		Mesh mesh = new Mesh();
		mesh.bind();
		{
			VBO vbo = new VBO();
			vbo.bind();
			{
				final float[] verts = new float[segments * 2];
				for (int i = 0; i < segments; ++i)
				{
					verts[i * 2] = (float) Math.cos(i * Transform.PI2 / segments);
					verts[i * 2 + 1] = (float) Math.sin(i * Transform.PI2 / segments);
				}
				vbo.putData(FloatBuffer.wrap(verts), false, GL15.GL_STATIC_DRAW);
			}
			vbo.unbind();
			mesh.setVertexArrayBuffer(0, 2, 0, vbo);
		}
		mesh.unbind();
		return mesh;
	}
}
