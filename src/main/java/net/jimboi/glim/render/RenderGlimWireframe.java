package net.jimboi.glim.render;

import net.jimboi.glim.RendererGlim;
import net.jimboi.mod.render.Render;

import org.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

/**
 * Created by Andy on 6/4/17.
 */
public class RenderGlimWireframe extends Render
{
	public RenderGlimWireframe(Program program)
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
	public void doRender(Program program, Model model, Material material, Instance inst)
	{
		Matrix4fc projectionMatrix = RendererGlim.CAMERA.projection();
		Matrix4fc viewMatrix = RendererGlim.CAMERA.view();
		Matrix4fc modelMatrix = inst.getRenderTransformation(_MAT_A);

		program.setUniform("u_model_view_projection", _MAT_B.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix));
		program.setUniform("u_model_view", _MAT_B.set(viewMatrix).mul(modelMatrix));
		program.setUniform("u_projection", projectionMatrix);
		program.setUniform("u_view", viewMatrix);
		program.setUniform("u_model", modelMatrix);

		program.setUniform("u_color", material.mainColor);

		GL11.glDrawElements(GL11.GL_LINE_LOOP, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, Material material)
	{
		if (material.hasTexture()) material.getTexture().unbind();
		model.unbind();
		program.unbind();
	}
}
