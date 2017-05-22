package net.jimboi.blob;

import net.jimboi.mod.Renderer;

import org.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;
import org.qsilver.render.Render;

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
	public void preRender(Program program, Model model, Material material)
	{
		program.bind();
		model.bind();
		if (material.hasTexture()) material.getTexture().bind();

		Matrix4fc projectionMatrix = Renderer.camera.projection();
		Matrix4fc viewMatrix = Renderer.camera.view();

		program.setUniform("projectionMatrix", projectionMatrix);
		program.setUniform("viewMatrix", viewMatrix);
	}

	@Override
	public void doRender(Program program, Model model, Material material, Instance inst)
	{
		program.setUniform("worldMatrix", inst.getRenderTransformation(_MAT_A));

		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, Material material)
	{
		program.unbind();
		model.unbind();
		if (material.hasTexture()) material.getTexture().unbind();
	}
}