package net.jimboi.canary.stage_a.owle;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.render.material.PropertyTexture;
import org.bstone.render.renderer.ProgramRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.Sprite;

/**
 * Created by Andy on 8/4/17.
 */
public class ConsoleProgramRenderer extends ProgramRenderer
{
	private final Matrix4f viewProjection = new Matrix4f();
	private final Matrix4f modelViewProjection = new Matrix4f();
	private final Vector4f color = new Vector4f();

	public ConsoleProgramRenderer()
	{
		super(new Program(), new Material());

		Shader vsh = new Shader(new ResourceLocation("gordo:simple.vsh"), GL20.GL_VERTEX_SHADER);
		Shader fsh = new Shader(new ResourceLocation("gordo:simple.fsh"), GL20.GL_FRAGMENT_SHADER);
		this.program.link(vsh, fsh);
		vsh.close();
		fsh.close();

		this.material.addProperty(PropertyTexture.PROPERTY);
		this.material.addProperty(PropertyColor.PROPERTY);
	}

	@Override
	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program;
		program.bind();

		this.viewProjection.set(projection).mul(view);
	}

	@Override
	public void unbind()
	{
		final Program program = this.program;
		program.unbind();
	}

	public void draw(Mesh mesh, Material material, Matrix4fc transformation)
	{
		this.draw(mesh,
				PropertyTexture.PROPERTY.getTexture(material),
				PropertyTexture.PROPERTY.getSpriteOffset(material),
				PropertyTexture.PROPERTY.getSpriteScale(material),
				PropertyTexture.PROPERTY.getTransparency(material),
				PropertyColor.PROPERTY.getNormalizedRGB(material),
				transformation);
	}

	public void draw(Mesh mesh, Sprite sprite, boolean transparency, Vector3fc color, Matrix4fc transformation)
	{
		this.draw(mesh, sprite.getTexture(), new Vector2f(sprite.getU(), sprite.getV()), new Vector2f(sprite.getWidth(), sprite.getHeight()), transparency, color, transformation);
	}

	public void draw(Mesh mesh, Asset<Texture> texture, Vector2fc spriteOffset, Vector2fc spriteScale, boolean transparency, Vector3fc color, Matrix4fc transformation)
	{
		final Program program = this.program;
		program.setUniform("u_model_view_projection", this.viewProjection.mul(transformation, this.modelViewProjection));
		mesh.bind();
		{
			if (color != null)
			{
				program.setUniform("u_color", this.color.set(color.x(), color.y(), color.z(), 1));
			}
			else
			{
				program.setUniform("u_color", this.color.set(0, 0, 0, 0));
			}

			if (texture != null)
			{
				program.setUniform("u_sampler", 0);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				texture.getSource().bind();
			}

			program.setUniform("u_sprite_offset", spriteOffset);
			program.setUniform("u_sprite_scale", spriteScale);
			program.setUniform("u_transparency", transparency);

			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			if (texture != null)
			{
				texture.getSource().unbind();
			}
		}
		mesh.unbind();
	}
}
