package net.jimboi.mod.meshbuilder;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.VBO;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

/**
 * Created by Andy on 4/30/17.
 */
public class ModelUtil
{
	public static void putVertexBuffer(Mesh mesh, float[] data, int location, int size, int stride)
	{
		FloatBuffer buffer = null;
		try
		{
			VBO vbo = new VBO();
			buffer = (FloatBuffer) MemoryUtil.memAllocFloat(data.length).put(data).flip();
			vbo.putData(buffer, false, GL_STATIC_DRAW);
			mesh.bindVertexArrayBuffer(location, size, stride, vbo);
		}
		finally
		{
			if (buffer != null)
			{
				MemoryUtil.memFree(buffer);
			}
		}
	}

	public static void putIndexBuffer(Mesh mesh, int[] data)
	{
		IntBuffer buffer = null;
		try
		{
			VBO ibo = new VBO(GL15.GL_ELEMENT_ARRAY_BUFFER);
			buffer = (IntBuffer) MemoryUtil.memAllocInt(data.length).put(data).flip();
			ibo.putData(buffer, false, GL_STATIC_DRAW);
			mesh.bindElementArrayBuffer(ibo);
		}
		finally
		{
			if (buffer != null)
			{
				MemoryUtil.memFree(buffer);
			}
		}
	}

	public static Mesh createMesh(MeshData data)
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, data.positions, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, data.texcoords, 1, 2, 0);
		ModelUtil.putVertexBuffer(mesh, data.normals, 2, 3, 0);
		ModelUtil.putIndexBuffer(mesh, data.indices);
		return mesh;
	}
}
