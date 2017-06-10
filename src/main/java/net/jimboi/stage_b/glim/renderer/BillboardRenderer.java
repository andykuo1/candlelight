package net.jimboi.stage_b.glim.renderer;

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

	private final Program program;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public BillboardRenderer(Camera camera, Type type)
	{
		this.camera = camera;
		this.type = type;

		Shader vs = new Shader(VERTEX_SHADER_LOCATION, GL20.GL_VERTEX_SHADER);
		Shader fs = new Shader(FRAGMENT_SHADER_LOCATION, GL20.GL_FRAGMENT_SHADER);
		Program prgm = new Program();
		prgm.link(vs, fs);
		this.program = prgm;
	}

	@Override
	public void close()
	{
		this.program.close();
	}

	public void render(Iterator<Instance> iterator)
	{
		Matrix4fc proj = this.camera.projection();
		Matrix4fc view = this.camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		Program program = this.program;

		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				Instance inst = iterator.next();
				Model model = inst.getModel().getSource();
				Material material = inst.getMaterial().getSource();

				Texture texture = null;
				Sprite sprite = null;

				if (material.hasComponent(PropertyTexture.class))
				{
					PropertyTexture propTexture = material.getComponent(PropertyTexture.class);
					texture = propTexture.texture;
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
