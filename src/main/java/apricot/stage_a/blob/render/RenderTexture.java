package apricot.stage_a.blob.render;

import apricot.stage_a.blob.RendererBlob;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.render.Render;

import apricot.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;

/**
 * Created by Andy on 4/30/17.
 */
public class RenderTexture extends Render
{
	public RenderTexture(Program program)
	{
		super(program);
	}

	@Override
	public void preRender(Program program, Model model, ModMaterial material)
	{
		program.bind();
		model.bind();
		if (material.hasTexture()) material.getTexture().bind();

		Matrix4fc projectionMatrix = RendererBlob.camera.projection();
		Matrix4fc viewMatrix = RendererBlob.camera.view();

		program.setUniform("projectionMatrix", projectionMatrix);
		program.setUniform("viewMatrix", viewMatrix);
	}

	@Override
	public void doRender(Program program, Model model, ModMaterial material, Instance inst)
	{
		program.setUniform("worldMatrix", inst.getRenderTransformation(_MAT_A));

		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, ModMaterial material)
	{
		program.unbind();
		model.unbind();
		if (material.hasTexture()) material.getTexture().unbind();
	}
}
