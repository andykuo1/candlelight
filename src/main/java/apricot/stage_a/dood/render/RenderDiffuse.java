package apricot.stage_a.dood.render;

import apricot.stage_a.dood.ResourcesDood;
import apricot.stage_a.mod.ModLight;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.render.Render;

import apricot.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

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
	public void preRender(Program program, Model model, ModMaterial material)
	{
		program.bind();
		model.bind();
		if (material.hasTexture()) material.getTexture().bind();
	}

	@Override
	public void doRender(Program program, Model model, ModMaterial material, Instance inst)
	{
		Matrix4fc projectionMatrix = ResourcesDood.INSTANCE.camera.projection();
		Matrix4fc viewMatrix = ResourcesDood.INSTANCE.camera.view();
		Matrix4fc modelMatrix = inst.getRenderTransformation(_MAT_A);

		program.setUniform("u_model_view_projection", _MAT_B.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix));
		program.setUniform("u_model_view", _MAT_B.set(viewMatrix).mul(modelMatrix));
		program.setUniform("u_projection", projectionMatrix);
		program.setUniform("u_view", viewMatrix);
		program.setUniform("u_model", modelMatrix);
		program.setUniform("u_camera_pos", ResourcesDood.INSTANCE.camera.transform().position3());

		if (material.sprite != null)
		{
			program.setUniform("u_tex_offset", new Vector2f(material.sprite.getU(), material.sprite.getV()));
			program.setUniform("u_tex_scale", new Vector2f(material.sprite.getWidth(), material.sprite.getHeight()));
		}
		else
		{
			program.setUniform("u_tex_offset", new Vector2f(0, 0));
			program.setUniform("u_tex_scale", new Vector2f(1, 1));
		}

		program.setUniform("u_shininess", material.shininess);
		program.setUniform("u_specular_color", material.specularColor);

		bindLightsToProgram(program, ResourcesDood.INSTANCE.lights);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, ModMaterial material)
	{
		if (material.hasTexture()) material.getTexture().unbind();
		model.unbind();
		program.unbind();
	}

	private static void bindLightsToProgram(Program program, Collection<ModLight> lights)
	{
		program.setUniform("u_light_count", lights.size());

		int i = 0;
		Iterator<ModLight> iter = lights.iterator();
		while (iter.hasNext())
		{
			ModLight light = iter.next();
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
