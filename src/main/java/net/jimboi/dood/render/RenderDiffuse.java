package net.jimboi.dood.render;

import net.jimboi.mod.Light;
import net.jimboi.mod.Renderer;

import org.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.qsilver.render.Instance;
import org.qsilver.render.Material;
import org.qsilver.render.Model;
import org.qsilver.render.Render;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Andy on 5/21/17.
 */
public class RenderDiffuse extends Render
{
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
	public void doRender(Program program, Model model, Material material, Instance inst)
	{
		Matrix4fc projectionMatrix = Renderer.camera.projection();
		Matrix4fc viewMatrix = Renderer.camera.view();
		Matrix4fc modelMatrix = inst.getRenderTransformation(_MAT_A);

		program.setUniform("u_model_view_projection", _MAT_B.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix));
		program.setUniform("u_model_view", _MAT_B.set(viewMatrix).mul(modelMatrix));
		program.setUniform("u_projection", projectionMatrix);
		program.setUniform("u_view", viewMatrix);
		program.setUniform("u_model", modelMatrix);
		program.setUniform("u_camera_pos", Renderer.camera.getTransform().position());

		if (material.sprite != null)
		{
			float rw = 1F / material.sprite.getTexture().width();
			float rh = 1F / material.sprite.getTexture().height();
			program.setUniform("u_tex_offset", new Vector2f(material.sprite.getU() * rw, material.sprite.getV() * rh));
			program.setUniform("u_tex_scale", new Vector2f(material.sprite.getWidth() * rw, material.sprite.getHeight() * rh));
		}
		else
		{
			program.setUniform("u_tex_offset", new Vector2f(0, 0));
			program.setUniform("u_tex_scale", new Vector2f(1, 1));
		}

		program.setUniform("u_shininess", material.shininess);
		program.setUniform("u_specular_color", material.specularColor);

		bindLightsToProgram(program, Renderer.lights);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, Material material)
	{
		if (material.hasTexture()) material.getTexture().unbind();
		model.unbind();
		program.unbind();
	}

	private static void bindLightsToProgram(Program program, Collection<Light> lights)
	{
		program.setUniform("u_light_count", lights.size());

		int i = 0;
		Iterator<Light> iter = lights.iterator();
		while (iter.hasNext())
		{
			Light light = iter.next();
			program.setUniform(lightUniform("position", i), light.position);
			program.setUniform(lightUniform("intensity", i), light.color);
			program.setUniform(lightUniform("attenuation", i), light.attentuation);
			program.setUniform(lightUniform("ambientCoefficient", i), light.ambientCoefficient);
			program.setUniform(lightUniform("coneAngle", i), light.coneAngle);
			program.setUniform(lightUniform("coneDirection", i), light.coneDirection);
			++i;
		}
	}

	private static String lightUniform(String name, int index)
	{
		return "u_light[" + index + "]." + name;
	}
}
