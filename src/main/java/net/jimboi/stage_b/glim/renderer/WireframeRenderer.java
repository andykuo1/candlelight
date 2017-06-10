package net.jimboi.stage_b.glim.renderer;

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

import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class WireframeRenderer implements AutoCloseable
{
	public static final ResourceLocation VERTEX_SHADER_LOCATION = new ResourceLocation("glim:wireframe.vsh");
	public static final ResourceLocation FRAGMENT_SHADER_LOCATION = new ResourceLocation("glim:wireframe.fsh");

	private final Camera camera;
	private final Shader vertexShader;
	private final Shader fragmentShader;
	private final Program program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public WireframeRenderer(Camera camera)
	{
		this.camera = camera;

		this.vertexShader = new Shader(VERTEX_SHADER_LOCATION, GL20.GL_VERTEX_SHADER);
		this.fragmentShader = new Shader(FRAGMENT_SHADER_LOCATION, GL20.GL_FRAGMENT_SHADER);
		this.program = new Program();
		this.program.link(this.vertexShader, this.fragmentShader);
	}

	@Override
	public void close()
	{
		this.vertexShader.close();
		this.fragmentShader.close();
		this.program.close();
	}

	public void render(Iterator<Instance> iterator)
	{
		Matrix4fc proj = this.camera.projection();
		Matrix4fc view = this.camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		this.program.bind();
		{
			this.program.setUniform("u_projection", proj);
			this.program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				Instance inst = iterator.next();
				Model model = inst.getModel().getSource();
				Material material = inst.getMaterial().getSource();

				Vector3f diffuseColor = null;

				if (material.hasComponent(PropertyDiffuse.class))
				{
					PropertyDiffuse propDiffuse = material.getComponent(PropertyDiffuse.class);
					diffuseColor = propDiffuse.diffuseColor;
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				this.program.setUniform("u_model", transformation);
				this.program.setUniform("u_model_view_projection", modelViewProj);

				model.bind();
				{
					program.setUniform("u_color", diffuseColor);

					GL11.glDrawElements(GL11.GL_LINE_LOOP, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				model.unbind();
			}
		}
		this.program.unbind();
	}
}
