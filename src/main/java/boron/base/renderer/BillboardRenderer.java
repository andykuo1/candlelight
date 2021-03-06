package boron.base.renderer;

import boron.base.asset.Asset;
import boron.base.material.OldMaterial;
import boron.base.render.OldModel;
import boron.base.render.OldRenderEngine;
import boron.base.render.OldRenderable;
import boron.base.render.OldishRenderService;
import boron.base.renderer.property.OldPropertyTexture;
import boron.base.sprite.Sprite;

import boron.bstone.camera.Camera;
import boron.bstone.mogli.Mesh;
import boron.bstone.mogli.Program;
import boron.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class BillboardRenderer extends OldishRenderService
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

	public BillboardRenderer(OldRenderEngine renderEngine, Asset<Program> program, Type type)
	{
		super(renderEngine);

		this.program = program;
		this.type = type;
	}

	@Override
	protected void onServiceStart(OldRenderEngine handler)
	{
	}

	@Override
	protected void onServiceStop(OldRenderEngine handler)
	{
	}

	@Override
	protected void onRenderUpdate(OldRenderEngine renderEngine, double delta)
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

				if (material.hasComponent(OldPropertyTexture.class))
				{
					OldPropertyTexture propTexture = material.getComponent(OldPropertyTexture.class);
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
