package net.jimboi.stage_b.glim.renderer;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;
import net.jimboi.stage_b.gnome.sprite.Sprite;

import org.bstone.camera.Camera;
import org.bstone.material.Material;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.model.Model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class BillboardRenderer implements AutoCloseable
{
	public enum Type
	{
		CYLINDRICAL,
		SPHERICAL
	}

	public static final ResourceLocation VERTEX_SHADER_LOCATION = new ResourceLocation("glim:billboard.vsh");
	public static final ResourceLocation FRAGMENT_SHADER_LOCATION = new ResourceLocation("glim:billboard.fsh");

	private final Camera camera;
	private final Type type;

	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public BillboardRenderer(Camera camera, Type type)
	{
		this.camera = camera;
		this.type = type;

		final AssetManager assetManager = RendererGlim.INSTANCE.getAssetManager();
		Asset<Shader> vs = assetManager.registerAsset(Shader.class, "v_billboard", new ShaderLoader.ShaderParameter(VERTEX_SHADER_LOCATION, GL20.GL_VERTEX_SHADER));
		Asset<Shader> fs = assetManager.registerAsset(Shader.class, "f_billboard", new ShaderLoader.ShaderParameter(FRAGMENT_SHADER_LOCATION, GL20.GL_FRAGMENT_SHADER));
		this.program = assetManager.registerAsset(Program.class, "billboard", new ProgramLoader.ProgramParameter(Arrays.asList(vs, fs)));
	}

	@Override
	public void close()
	{
	}

	public void render(Iterator<Instance> iterator)
	{
		Matrix4fc proj = this.camera.projection();
		Matrix4fc view = this.camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final Instance inst = iterator.next();
				final Model model = inst.getModel().getSource();
				final Material material = inst.getMaterial().getSource();

				Texture texture = null;
				Sprite sprite = null;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.texture.getSource();
					sprite = propTexture.sprite;
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				model.bind();
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

					program.setUniform("u_billboard_type", this.type.ordinal());

					GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

					if (texture != null)
					{
						texture.unbind();
					}
				}
				model.unbind();
			}
		}
		program.unbind();
	}
}
