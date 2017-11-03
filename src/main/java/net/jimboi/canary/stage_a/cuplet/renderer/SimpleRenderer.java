package net.jimboi.canary.stage_a.cuplet.renderer;

import org.bstone.asset.Asset;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Created by Andy on 10/31/17.
 */
public class SimpleRenderer extends AbstractRenderer
{
	private final Matrix4f viewProjection = new Matrix4f();
	private final Matrix4f modelViewProjection = new Matrix4f();

	public SimpleRenderer(Asset<Program> program)
	{
		super(program);
	}

	@Override
	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program.get();
		program.bind();

		this.viewProjection.set(projection).mul(view);
	}

	@Override
	public void unbind()
	{
		final Program program = this.program.get();
		program.unbind();
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
			material.applyOrDefault(MaterialProperty.COLOR, program, new Vector4f(1, 1, 1, 1));

			boolean flag = material.apply(MaterialProperty.TEXTURE, program);

			material.applyOrDefault(MaterialProperty.SPRITE_OFFSET, program, new Vector2f());
			material.applyOrDefault(MaterialProperty.SPRITE_SCALE, program, new Vector2f(1, 1));
			material.applyOrDefault(MaterialProperty.TRANSPARENCY, program, true);

			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			//Clean up
			if (flag)
			{
				Asset<Texture> texture = material.getProperty(MaterialProperty.TEXTURE);
				texture.get().unbind();
			}
		}
		mesh.unbind();
	}
}
