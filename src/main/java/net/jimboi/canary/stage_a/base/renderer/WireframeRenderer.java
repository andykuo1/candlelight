package net.jimboi.canary.stage_a.base.renderer;

import org.bstone.asset.Asset;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Created by Andy on 11/1/17.
 */
public class WireframeRenderer extends AbstractRenderer
{
	private final Matrix4f viewProjection = new Matrix4f();
	private final Matrix4f modelViewProjection = new Matrix4f();

	public WireframeRenderer(Asset<Program> program)
	{
		super(program);
	}

	@Override
	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program.get();
		program.bind();

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		this.viewProjection.set(projection).mul(view);
	}

	@Override
	public void unbind()
	{
		final Program program = this.program.get();
		program.unbind();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public void draw(Asset<Mesh> modelMesh, Material material, Matrix4fc transformation)
	{
		final Program program = this.program.get();
		final Mesh mesh = modelMesh.get();

		MaterialProperty.MODELVIEWPROJECTION.apply(program,
				this.viewProjection.mul(transformation, this.modelViewProjection));

		mesh.bind();
		{
			material.applyOrDefault(MaterialProperty.COLOR, program, new Vector4f(1, 1, 1, 0));

			GL11.glDrawElements(GL11.GL_LINE_LOOP, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		mesh.unbind();
	}
}
