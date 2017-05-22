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
public class RenderDiffuse extends Render
{
	private static Material MATERIAL;

	public RenderDiffuse(Program program)
	{
		super(program);
	}

	@Override
	public void preRender(Program program, Model model, Material material)
	{
		program.bind();
		model.bind();
		if (material.hasTexture()) material.getTexture().bind();
	}

	@Override
	public void postRender(Program program, Model model, Material material)
	{
		if (material.hasTexture()) material.getTexture().unbind();
		model.unbind();
		program.unbind();
	}

	@Override
	public void doRender(Program program, Model model, Material material, Instance inst)
	{
		Matrix4fc projectionMatrix = Renderer.camera.projection();
		Matrix4fc viewMatrix = Renderer.camera.view();

		program.setUniform("projectionMatrix", projectionMatrix);
		program.setUniform("viewMatrix", viewMatrix);
		program.setUniform("worldMatrix", inst.getRenderTransformation(_MAT_A));

		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}
}