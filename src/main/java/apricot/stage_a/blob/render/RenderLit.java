package apricot.stage_a.blob.render;

import apricot.stage_a.blob.RendererBlob;
import apricot.stage_a.mod.ModLight;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.render.Render;

import apricot.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Andy on 4/30/17.
 */
public class RenderLit extends Render
{
	public RenderLit(Program program)
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
	public void postRender(Program program, Model model, ModMaterial material)
	{
		program.unbind();
		model.unbind();
		if (material.hasTexture()) material.getTexture().unbind();
	}

	@Override
	public void doRender(Program program, Model model, ModMaterial material, Instance inst)
	{

		Matrix4fc projectionMatrix = RendererBlob.camera.projection();
		Matrix4fc viewMatrix = RendererBlob.camera.view();

		program.setUniform("projectionMatrix", projectionMatrix);
		program.setUniform("viewMatrix", viewMatrix);
		program.setUniform("worldMatrix", inst.getRenderTransformation(_MAT_A));
		program.setUniform("cameraPosition", RendererBlob.camera.transform().position3());

		program.setUniform("shininess", material.shininess);
		program.setUniform("specularColor", material.specularColor);

		bindLightsToProgram(program, RendererBlob.lights);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
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
