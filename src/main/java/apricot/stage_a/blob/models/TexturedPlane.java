package apricot.stage_a.blob.models;

import apricot.stage_a.mod.model.Model;

import apricot.bstone.mogli.Mesh;
import org.lwjgl.opengl.GL15;
import apricot.zilar.meshbuilder.ModelUtil;

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
		ModelUtil.putVertexBuffer(mesh, POSITIONS, GL15.GL_STATIC_DRAW, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, TEXCOORDS, GL15.GL_STATIC_DRAW, 1, 2, 0);
		ModelUtil.putIndexBuffer(mesh, INDICES, GL15.GL_STATIC_DRAW);
		Model model = new Model(mesh);
		return model;
	}
}
