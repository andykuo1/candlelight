package net.jimboi.apricot.base.renderer;

import net.jimboi.apricot.base.renderer.property.PropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.render.Renderable;
import org.bstone.render.model.Model;
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
import org.zilar.sprite.Sprite;

import java.util.Iterator;

/**
 * Created by Andy on 7/4/17.
 */
public class SimpleRenderer extends RenderService
{
	private final Asset<Program> program;
	private final Camera camera;
	private final Iterable<Renderable> renderables;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public SimpleRenderer(RenderEngine renderEngine, Asset<Program> program, Camera camera, Iterable<Renderable> renderables)
	{
		super(renderEngine);
		this.program = program;
		this.camera = camera;
		this.renderables = renderables;
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		//this.render(this.camera, this.renderables.iterator());
	}

	public void render(Camera camera, Iterator<Renderable> iterator)
	{
		Matrix4fc u_model_view_projection;
		Matrix4fc u_model;
		final Matrix4fc u_view = camera.view();
		final Matrix4fc u_projection = camera.projection();

		Vector2fc def_tex_offset = new Vector2f();
		Vector2fc def_tex_scale = new Vector2f(1, 1);

		int def_sampler = 0;
		boolean def_transparency = false;
		Vector4fc def_diffuse_color = new Vector4f(1, 1, 1, 0);

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", u_projection);
			program.setUniform("u_view", u_view);

			while (iterator.hasNext())
			{
				final Renderable inst = iterator.next();
				if (!inst.isRenderVisible()) continue;

				final Model model = inst.getRenderModel();
				final Mesh mesh = model.getMesh().getSource();
				final Material material = model.getMaterial();

				Texture texture = null;
				Sprite sprite = null;

				int u_sampler = def_sampler;
				boolean u_transparency = def_transparency;
				Vector4fc u_diffuse_color = def_diffuse_color;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propertyTexture = material.getComponent(PropertyTexture.class);
					texture = propertyTexture.getTexture().getSource();
					sprite = propertyTexture.getSprite();
					u_transparency = propertyTexture.isTransparent();
				}
				program.setUniform("u_transparency", u_transparency);

				if (material.hasComponent(PropertyDiffuse.class))
				{
					PropertyDiffuse propertyDiffuse = material.getComponent(PropertyDiffuse.class);
					u_diffuse_color = propertyDiffuse.diffuseColor;
				}
				program.setUniform("u_diffuse_color", u_diffuse_color);

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

					Vector2fc u_tex_offset = def_tex_offset;
					Vector2fc u_tex_scale = def_tex_scale;

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
