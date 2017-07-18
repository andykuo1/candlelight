package net.jimboi.apricot.stage_a.blob.render;

import net.jimboi.apricot.stage_a.blob.RendererBlob;
import net.jimboi.apricot.stage_a.mod.ModMaterial;
import net.jimboi.apricot.stage_a.mod.instance.Instance;
import net.jimboi.apricot.stage_a.mod.model.Model;
import net.jimboi.apricot.stage_a.mod.render.Render;

import org.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;

/**
 * Created by Andy on 4/30/17.
 */
public class RenderColor extends Render
{
	public RenderColor(Program program)
	{
		super(program);
	}

	@Override
	public void preRender(Program program, Model model, ModMaterial material)
	{
		program.bind();
		model.getMesh().bind();
	}

	@Override
	public void postRender(Program program, Model model, ModMaterial material)
	{
		program.unbind();
		model.getMesh().unbind();
	}

	@Override
	public void doRender(Program program, Model model, ModMaterial material, Instance inst)
	{
		Matrix4fc projectionMatrix = RendererBlob.camera.projection();
		Matrix4fc viewMatrix = RendererBlob.camera.view();

		program.setUniform("projectionMatrix", projectionMatrix);
		program.setUniform("viewMatrix", viewMatrix);
		program.setUniform("worldMatrix", inst.getRenderTransformation(_MAT_A));

		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}
}
