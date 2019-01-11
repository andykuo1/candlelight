package apricot.stage_a.dood.render;

import apricot.stage_a.dood.ResourcesDood;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.render.Render;

import apricot.bstone.mogli.Program;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

/**
 * Created by Andy on 5/20/17.
 */
public class RenderBillboard extends Render
{
	public enum Type
	{
		CYLINDRICAL,
		SPHERICAL
	}

	private final Type type;

	public RenderBillboard(Program program, Type type)
	{
		super(program);

		this.type = type;
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

		program.setUniform("u_billboard_type", this.type.ordinal());

		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	}

	@Override
	public void postRender(Program program, Model model, ModMaterial material)
	{
		if (material.hasTexture()) material.getTexture().unbind();
		model.unbind();
		program.unbind();
	}
}
