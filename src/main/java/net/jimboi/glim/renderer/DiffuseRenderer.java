package net.jimboi.glim.renderer;

import net.jimboi.glim.RendererGlim;
import net.jimboi.mod.Light;
import net.jimboi.mod.instance.Instance;
import net.jimboi.mod2.material.property.PropertySpecular;
import net.jimboi.mod2.material.property.PropertyTexture;
import net.jimboi.mod2.resource.ResourceLocation;
import net.jimboi.mod2.sprite.Sprite;

import org.bstone.camera.Camera;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.material.Material;
import org.qsilver.model.Model;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Andy on 6/7/17.
 */
public class DiffuseRenderer implements AutoCloseable
{
	public static final ResourceLocation VERTEX_SHADER_LOCATION = new ResourceLocation("glim:diffuse.vsh");
	public static final ResourceLocation FRAGMENT_SHADER_LOCATION = new ResourceLocation("glim:diffuse.fsh");

	private final Camera camera;
	private final Shader vertexShader;
	private final Shader fragmentShader;
	private final Program program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public DiffuseRenderer(Camera camera)
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
				Model model = inst.getModel();
				Material material = inst.getMaterial();

				Texture texture = null;
				Sprite sprite = null;
				Vector3f specularColor = null;
				float shininess = 0;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.texture;
					sprite = propTexture.sprite;
				}

				if (material.hasComponent(PropertySpecular.class))
				{
					PropertySpecular propSpecular = material.getComponent(PropertySpecular.class);
					specularColor = propSpecular.specularColor;
					shininess = propSpecular.shininess;
				}

				Matrix4fc transformation = inst.getRenderTransformation(this.modelMatrix);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				this.program.setUniform("u_model", transformation);
				this.program.setUniform("u_model_view_projection", modelViewProj);

				model.bind();
				{
					if (texture != null)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0);
						texture.bind();
					}

					this.program.setUniform("u_sampler", 0);
					this.program.setUniform("u_camera_pos", this.camera.getTransform().position());

					if (sprite != null)
					{
						this.program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
						this.program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));
					}

					if (specularColor != null)
					{
						this.program.setUniform("u_shininess", shininess);
						this.program.setUniform("u_specular_color", specularColor);
					}

					bindLightsToProgram(program, RendererGlim.LIGHTS);
					GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

					if (texture != null)
					{
						texture.unbind();
					}
				}
				model.unbind();
			}
		}
		this.program.unbind();
	}

	private static void bindLightsToProgram(Program program, Collection<Light> lights)
	{
		program.setUniform("u_light_count", lights.size());

		int i = 0;
		Iterator<Light> iter = lights.iterator();
		while (iter.hasNext())
		{
			Light light = iter.next();
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
