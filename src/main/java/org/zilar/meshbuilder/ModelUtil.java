package org.zilar.meshbuilder;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.VBO;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Andy on 4/30/17.
 */
public class ModelUtil
{
	public static void putVertexBuffer(Mesh mesh, FloatBuffer data, int usage, int location, int size, int stride)
	{
		VBO vbo = new VBO();
		vbo.bind();
		{
			vbo.putData(data, false, usage);
			mesh.bindVertexArrayBuffer(location, size, stride, vbo);
		}
		vbo.unbind();
	}

	public static void putIndexBuffer(Mesh mesh, IntBuffer data, int usage)
	{
		VBO ibo = new VBO(GL15.GL_ELEMENT_ARRAY_BUFFER);
		ibo.bind();
		{
			ibo.putData(data, false, usage);
			mesh.bindElementArrayBuffer(ibo);
		}
		ibo.unbind();
	}

	public static void putVertexBuffer(Mesh mesh, float[] data, int usage, int location, int size, int stride)
	{
		FloatBuffer buffer = null;
		try
		{
			VBO vbo = new VBO();
			buffer = (FloatBuffer) MemoryUtil.memAllocFloat(data.length).put(data).flip();
			vbo.putData(buffer, false, usage);
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

	public static void putIndexBuffer(Mesh mesh, int[] data, int usage)
	{
		IntBuffer buffer = null;
		try
		{
			VBO ibo = new VBO(GL15.GL_ELEMENT_ARRAY_BUFFER);
			buffer = (IntBuffer) MemoryUtil.memAllocInt(data.length).put(data).flip();
			ibo.putData(buffer, false, usage);
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

	public static Mesh createStaticMesh(MeshData data)
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, data.getPositions(), GL15.GL_STATIC_DRAW, 0, data.getPositionVertexSize(), 0);
		ModelUtil.putVertexBuffer(mesh, data.getTexcoords(), GL15.GL_STATIC_DRAW, 1, data.getTexcoordVertexSize(), 0);
		ModelUtil.putVertexBuffer(mesh, data.getNormals(), GL15.GL_STATIC_DRAW, 2, data.getNormalVertexSize(), 0);
		ModelUtil.putIndexBuffer(mesh, data.getIndices(), GL15.GL_STATIC_DRAW);
		return mesh;
	}

	public static Mesh createDynamicMesh(MeshData data)
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, data.getPositions(), GL15.GL_DYNAMIC_DRAW, 0, data.getPositionVertexSize(), 0);
		ModelUtil.putVertexBuffer(mesh, data.getTexcoords(), GL15.GL_DYNAMIC_DRAW, 1, data.getTexcoordVertexSize(), 0);
		ModelUtil.putVertexBuffer(mesh, data.getNormals(), GL15.GL_DYNAMIC_DRAW, 2, data.getNormalVertexSize(), 0);
		ModelUtil.putIndexBuffer(mesh, data.getIndices(), GL15.GL_DYNAMIC_DRAW);
		return mesh;
	}

	public static Mesh updateMesh(Mesh mesh, MeshData data)
	{
		mesh.getVertexBuffer(0).updateData(data.getPositions(), 0);
		mesh.getVertexBuffer(1).updateData(data.getTexcoords(), 0);
		mesh.getVertexBuffer(2).updateData(data.getNormals(), 0);
		mesh.getIndexBuffer().updateData(data.getIndices(), 0);
		return mesh;
	}
}
