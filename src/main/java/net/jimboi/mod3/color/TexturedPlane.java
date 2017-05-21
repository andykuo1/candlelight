package net.jimboi.mod3.color;

import net.jimboi.mod3.meshbuilder.ModelUtil;

import org.bstone.mogli.Mesh;
import org.qsilver.render.Model;

/**
 * Created by Andy on 4/27/17.
 */
public class TexturedPlane
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

	private static final float[] TEXCOORDS = new float[]{
			//V0
			0.5F, 0.0F,
			//V1
			0.0F, 1.0F,
			//V2
			1.0F, 1.0F,
			//V3
			1.0F, 0.0F
	};

	private static final int[] INDICES = new int[]{
			0, 1, 3, 2, 3, 1
	};

	public static Model create()
	{
		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, POSITIONS, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, TEXCOORDS, 1, 2, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES);
		Model model = new Model(mesh);
		return model;
	}
}
