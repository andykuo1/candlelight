package net.jimboi.apricot.stage_a.blob.models;

import net.jimboi.apricot.stage_a.mod.model.Model;

import org.bstone.mogli.Mesh;
import org.lwjgl.opengl.GL15;
import org.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 4/27/17.
 */
public class ColoredPlane
{
	private static final float[] POSITIONS = new float[]{
			// VO
			-0.5f,  0.5f, 0,
			// V1
			-0.5f, -0.5f, 0,
			// V2
			0.5f, -0.5f, 0,
			// V3
			0.5f,  0.5f, 0
	};

	private static final float[] COLORS = new float[]{
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f
	};

	private static final int[] INDICES = new int[]{
			0, 1, 3, 2, 3, 1
	};

	public static Model create()
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, POSITIONS, GL15.GL_STATIC_DRAW, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, COLORS, GL15.GL_STATIC_DRAW, 1, 3, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES, GL15.GL_STATIC_DRAW);
		Model model = new Model(mesh);
		return model;
	}
}
