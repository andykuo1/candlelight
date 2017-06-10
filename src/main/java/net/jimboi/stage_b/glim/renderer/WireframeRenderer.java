package net.jimboi.stage_b.glim.renderer;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.resourceloader.ProgramLoader;
import net.jimboi.stage_b.glim.resourceloader.ShaderLoader;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.asset.AssetManager;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyDiffuse;
import net.jimboi.stage_b.gnome.resource.ResourceLocation;

import org.bstone.camera.Camera;
import org.bstone.material.Material;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.qsilver.model.Model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class WireframeRenderer implements AutoCloseable
{
	public static final ResourceLocation VERTEX_SHADER_LOCATION = new ResourceLocation("glim:wireframe.vsh");
	public static final ResourceLocation FRAGMENT_SHADER_LOCATION = new ResourceLocation("glim:wireframe.fsh");

	private final Camera camera;
	private final Asset<Program> program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public WireframeRenderer(Camera camera)
	{
		this.camera = camera;

		final AssetManager assetManager = RendererGlim.INSTANCE.getAssetManager();
		Asset<Shader> vs = assetManager.registerAsset(Shader.class, "v_wireframe", new ShaderLoader.ShaderParameter(VERTEX_SHADER_LOCATION, GL20.GL_VERTEX_SHADER));
		Asset<Shader> fs = assetManager.registerAsset(Shader.class, "f_wireframe", new ShaderLoader.ShaderParameter(FRAGMENT_SHADER_LOCATION, GL20.GL_FRAGMENT_SHADER));
		this.program = assetManager.registerAsset(Program.class, "wireframe", new ProgramLoader.ProgramParameter(Arrays.asList(vs, fs)));
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

				Vector3f diffuseColor = null;

				if (material.hasComponent(PropertyDiffuse.class))
				{
					PropertyDiffuse propDiffuse = material.getComponent(PropertyDiffuse.class);
					diffuseColor = propDiffuse.diffuseColor;
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				model.bind();
				{
					program.setUniform("u_color", diffuseColor);

					GL11.glDrawElements(GL11.GL_LINE_LOOP, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				model.unbind();
			}
		}
		program.unbind();
	}
}
