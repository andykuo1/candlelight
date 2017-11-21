package net.jimboi.boron.base_ab.render.renderer;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.render.material.Material;
import net.jimboi.boron.base_ab.render.material.PropertyColor;
import net.jimboi.boron.base_ab.render.material.PropertyTexture;
import net.jimboi.boron.base_ab.sprite.Sprite;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 8/4/17.
 */
public class BillboardProgramRenderer extends ProgramRenderer
{
	public enum Type
	{
		CYLINDRICAL,
		SPHERICAL
	}

	private final Matrix4f view = new Matrix4f();
	private final Matrix4f modelView = new Matrix4f();
	private final Vector4f color = new Vector4f();

	public BillboardProgramRenderer()
	{
		super(new Program(), new Material());

		try (Shader vsh = new Shader(new ResourceLocation("base:billboard.vsh"), GL20.GL_VERTEX_SHADER))
		{
			try (Shader fsh = new Shader(new ResourceLocation("base:billboard.fsh"), GL20.GL_FRAGMENT_SHADER))
			{
				this.program.link(vsh, fsh);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.material.addProperty(PropertyTexture.PROPERTY);
		this.material.addProperty(PropertyColor.PROPERTY);
	}

	@Override
	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program;
		program.bind();

		program.setUniform("u_projection", projection);
		this.view.set(view);
	}

	@Override
	public void unbind()
	{
		final Program program = this.program;
		program.unbind();
	}

	@Override
	public void draw(Mesh mesh, Material material, Matrix4fc transformation)
	{
		this.draw(Type.CYLINDRICAL, mesh, material, transformation);
	}

	public void draw(Type type, Mesh mesh, Material material, Matrix4fc transformation)
	{
		this.draw(type, mesh,
				PropertyTexture.PROPERTY.getTexture(material),
				PropertyTexture.PROPERTY.getSpriteOffset(material),
				PropertyTexture.PROPERTY.getSpriteScale(material),
				PropertyTexture.PROPERTY.getTransparency(material),
				PropertyColor.PROPERTY.getNormalizedRGB(material),
				transformation);
	}

	public void draw(Type type, Mesh mesh, Sprite sprite, boolean transparency, Vector3fc color, Matrix4fc transformation)
	{
		this.draw(type, mesh, sprite.getTexture(), new Vector2f(sprite.getU(), sprite.getV()), new Vector2f(sprite.getWidth(), sprite.getHeight()), transparency, color, transformation);
	}

	public void draw(Type type, Mesh mesh, Asset<Texture> texture, Vector2fc spriteOffset, Vector2fc spriteScale, boolean transparency, Vector3fc color, Matrix4fc transformation)
	{
		final Program program = this.program;
		final Texture currentTexture = texture == null ? null : texture.getSource();

		program.setUniform("u_model_view", this.view.mul(transformation, this.modelView));
		mesh.bind();
		{
			program.setUniform("u_billboard_type", type.ordinal());
			program.setUniform("u_color", this.color.set(color.x(), color.y(), color.z(), 1));

			if (currentTexture != null)
			{
				program.setUniform("u_sampler", 0);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				currentTexture.bind();
			}

			program.setUniform("u_sprite_offset", spriteOffset);
			program.setUniform("u_sprite_scale", spriteScale);
			program.setUniform("u_transparency", transparency);

			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			if (currentTexture != null)
			{
				currentTexture.unbind();
			}
		}
		mesh.unbind();
	}
}
