package net.jimboi.apricot.base.renderer;

import net.jimboi.apricot.base.material.OldMaterial;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.render.OldRenderable;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.window.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.qsilver.asset.Asset;
import org.zilar.sprite.Sprite;

import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class BillboardRenderer extends RenderService
{
	public enum Type
	{
		CYLINDRICAL,
		SPHERICAL
	}

	private final Asset<Program> program;
	private final Type type;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public BillboardRenderer(RenderEngine renderEngine, Asset<Program> program, Type type)
	{
		super(renderEngine);

		this.program = program;
		this.type = type;
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

	}

	public void render(Camera camera, Iterator<OldRenderable> iterator)
	{
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final OldRenderable inst = iterator.next();
				if (!inst.isRenderVisible()) continue;

				final OldModel model = inst.getRenderModel();
				final Mesh mesh = model.getMesh().getSource();
				final OldMaterial material = model.getMaterial();

				Texture texture = null;
				Sprite sprite = null;
				boolean transparency = false;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.getTexture().getSource();
					sprite = propTexture.getSprite();
					transparency = propTexture.isTransparent();
				}
				program.setUniform("u_transparency", transparency);

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelView = view.mul(transformation, this.modelViewMatrix).m00(1).m01(0).m02(0).m20(0).m21(0).m22(1);
				if (this.type == Type.SPHERICAL)
				{
					((Matrix4f) modelView).m10(0).m11(1).m12(0);
				}

				Matrix4fc modelViewProj = proj.mul(modelView, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					if (texture != null)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.bind();
					}

					program.setUniform("u_sampler", 0);

					if (sprite != null)
					{
						program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
						program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));
					}

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
