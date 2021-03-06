package apricot.stage_a.blob.models;

import apricot.stage_a.mod.model.Model;

import apricot.bstone.mogli.Mesh;
import org.lwjgl.opengl.GL15;
import apricot.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 4/27/17.
 */
public class DiffusedCube
{
	private static final float[] POSITIONS = {
			// bottom
			-1.0f,-1.0f,-1.0f,
			 1.0f,-1.0f,-1.0f,
			-1.0f,-1.0f, 1.0f,
			 1.0f,-1.0f,-1.0f,
			 1.0f,-1.0f, 1.0f,
			-1.0f,-1.0f, 1.0f,

			// top
			-1.0f, 1.0f,-1.0f,
			-1.0f, 1.0f, 1.0f,
			 1.0f, 1.0f,-1.0f,
			 1.0f, 1.0f,-1.0f,
			-1.0f, 1.0f, 1.0f,
			 1.0f, 1.0f, 1.0f,

			// front
			-1.0f,-1.0f, 1.0f,
			 1.0f,-1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,
			 1.0f,-1.0f, 1.0f,
			 1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,

			// back
			-1.0f,-1.0f,-1.0f,
			-1.0f, 1.0f,-1.0f,
			 1.0f,-1.0f,-1.0f,
			 1.0f,-1.0f,-1.0f,
			-1.0f, 1.0f,-1.0f,
			 1.0f, 1.0f,-1.0f,

			// left
			-1.0f,-1.0f, 1.0f,
			-1.0f, 1.0f,-1.0f,
			-1.0f,-1.0f,-1.0f,
			-1.0f,-1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f,-1.0f,

			// right
			1.0f,-1.0f, 1.0f,
			1.0f,-1.0f,-1.0f,
			1.0f, 1.0f,-1.0f,
			1.0f,-1.0f, 1.0f,
			1.0f, 1.0f,-1.0f,
			1.0f, 1.0f, 1.0f,
	};

	private static final float[] TEXCOORDS = {
			// bottom
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,

			// top
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,

			// front
			1.0f, 0.0f,
			0.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,

			//back
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
			1.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,

			// left
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,

			// right
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			0.0f, 1.0f
	};

	private static final float[] NORMALS = {
			//top
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,
			0.0f, -1.0f, 0.0f,

			// bottom
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,

			// front
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,

			// back
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,
			0.0f, 0.0f, -1.0f,

			// left
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,

			// right
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f
	};

	private static final int[] INDICES = {
			//bottom
			0, 1, 2, 3, 4, 5,

			// top
			6, 7, 8, 9, 10, 11,

			// front
			12, 13, 14, 15, 16, 17,

			// back
			18, 19, 20, 21, 22, 23,

			// left
			24, 25, 26, 27, 28, 29,

			// right
			30, 31, 32, 33, 34, 35
	};

	public static Model create()
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, POSITIONS, GL15.GL_STATIC_DRAW, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, TEXCOORDS, GL15.GL_STATIC_DRAW, 1, 2, 0);
		ModelUtil.putVertexBuffer(mesh, NORMALS, GL15.GL_STATIC_DRAW, 2, 3, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES, GL15.GL_STATIC_DRAW);
		Model model = new Model(mesh);
		return model;
	}
}
