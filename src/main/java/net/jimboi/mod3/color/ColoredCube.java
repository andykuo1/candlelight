package net.jimboi.mod3.color;

import net.jimboi.mod3.meshbuilder.ModelUtil;

import org.bstone.mogli.Mesh;
import org.qsilver.render.Model;

/**
 * Created by Andy on 4/27/17.
 */
public class ColoredCube
{
	private static final float[] POSITIONS = new float[]{
			// VO
			-0.5f,  0.5f,  0.5f,
			// V1
			-0.5f, -0.5f,  0.5f,
			// V2
			0.5f, -0.5f,  0.5f,
			// V3
			0.5f,  0.5f,  0.5f,
			// V4
			-0.5f,  0.5f, -0.5f,
			// V5
			0.5f,  0.5f, -0.5f,
			// V6
			-0.5f, -0.5f, -0.5f,
			// V7
			0.5f, -0.5f, -0.5f,
	};

	private static final float[] COLORS = new float[]{
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f,
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f,
	};

	private static final int[] INDICES = new int[]{
			// Front face
			0, 1, 3, 2, 3, 1,
			// Top Face
			0, 3, 4, 5, 4, 3,
			// Right face
			2, 7, 3, 5, 3, 7,
			// Left face
			1, 0, 6, 4, 6, 0,
			// Bottom face
			1, 6, 2, 7, 2, 6,
			// Back face
			6, 4, 7, 5, 7, 4,
	};

	public static Model create()
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, POSITIONS, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, COLORS, 1, 3, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES);
		Model model = new Model(mesh);
		return model;
	}
}
