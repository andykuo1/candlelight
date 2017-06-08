package net.jimboi.blob.models;

import net.jimboi.mod2.meshbuilder.ModelUtil;

import org.bstone.mogli.Mesh;
import org.qsilver.model.Model;

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
		ModelUtil.putVertexBuffer(mesh, POSITIONS, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, COLORS, 1, 3, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES);
		Model model = new Model(mesh);
		return model;
	}
}
