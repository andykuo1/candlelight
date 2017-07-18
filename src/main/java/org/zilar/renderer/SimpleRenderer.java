package org.zilar.renderer;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.window.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;
import org.qsilver.render.Renderable;
import org.zilar.model.Model;
import org.zilar.renderer.property.PropertyTexture;
import org.zilar.sprite.Sprite;

import java.util.Iterator;

/**
 * Created by Andy on 7/4/17.
 */
public class SimpleRenderer extends RenderService
{
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public SimpleRenderer(Asset<Program> program)
	{
		this.program = program;
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
	}

	public void render(Camera camera, Iterator<Renderable> iterator)
	{
		Matrix4fc u_model_view_projection;
		Matrix4fc u_model;
		final Matrix4fc u_view = camera.view();
		final Matrix4fc u_projection = camera.projection();

		Vector2fc u_tex_offset = new Vector2f();
		Vector2fc u_tex_scale = new Vector2f(1, 1);

		int u_sampler = 0;
		boolean u_transparency = false;
		Vector4fc u_diffuse_color = new Vector4f(1, 1, 1, 0);

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", u_projection);
			program.setUniform("u_view", u_view);
			program.setUniform("u_diffuse_color", u_diffuse_color);

			while (iterator.hasNext())
			{
				final Renderable inst = iterator.next();
				if (!inst.isVisible()) continue;

				final Model model = inst.getModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				Texture texture = null;
				Sprite sprite = null;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.getTexture().getSource();
					sprite = propTexture.getSprite();
					u_transparency = propTexture.transparent;
				}

				program.setUniform("u_transparency", u_transparency);

				u_model = inst.getRenderTransformation(this.modelMatrix);
				u_model_view_projection = u_projection.mul(u_view, this.modelViewProjMatrix).mul(u_model, this.modelViewProjMatrix);

				program.setUniform("u_model", u_model);
				program.setUniform("u_model_view_projection", u_model_view_projection);

				mesh.bind();
				{
					if (texture != null)
					{
						u_sampler = 0;
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.bind();
					}

					program.setUniform("u_sampler", u_sampler);

					if (sprite != null)
					{
						u_tex_offset = new Vector2f(sprite.getU(), sprite.getV());
						u_tex_scale = new Vector2f(sprite.getWidth(), sprite.getHeight());
					}

					program.setUniform("u_tex_offset", u_tex_offset);
					program.setUniform("u_tex_scale", u_tex_scale);

					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

					if (texture != null)
					{
						texture.unbind();
					}
				}
				mesh.unbind();
			}
		}
		program.unbind();
	}
}
