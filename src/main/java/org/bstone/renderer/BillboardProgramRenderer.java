package org.bstone.renderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.bstone.mogli.Texture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.qsilver.asset.Asset;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.Sprite;

/**
 * Created by Andy on 8/4/17.
 */
public class BillboardProgramRenderer implements AutoCloseable
{
	public enum Type
	{
		CYLINDRICAL,
		SPHERICAL
	}

	protected final Program program;

	private final Matrix4f view = new Matrix4f();
	private final Matrix4f modelView = new Matrix4f();

	public BillboardProgramRenderer()
	{
		Shader vsh = new Shader(new ResourceLocation("base:billboard.vsh"), GL20.GL_VERTEX_SHADER);
		Shader fsh = new Shader(new ResourceLocation("base:billboard.fsh"), GL20.GL_FRAGMENT_SHADER);
		this.program = new Program();
		this.program.link(vsh, fsh);
		vsh.close();
		fsh.close();
	}

	@Override
	public void close()
	{
		this.program.close();
	}

	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program;
		program.bind();

		program.setUniform("u_projection", projection);
		this.view.set(view);
	}

	public void unbind()
	{
		final Program program = this.program;
		program.unbind();
	}

	public void draw(Type type, Mesh mesh, Sprite sprite, boolean transparency, Vector4fc color, Matrix4fc transformation)
	{
		this.draw(type, mesh, sprite.getTexture(), new Vector2f(sprite.getU(), sprite.getV()), new Vector2f(sprite.getWidth(), sprite.getHeight()), transparency, color, transformation);
	}

	public void draw(Type type, Mesh mesh, Asset<Texture> texture, Vector2fc spriteOffset, Vector2fc spriteScale, boolean transparency, Vector4fc color, Matrix4fc transformation)
	{
		final Program program = this.program;
		program.setUniform("u_model_view", this.view.mul(transformation, this.modelView));
		mesh.bind();
		{
			program.setUniform("u_billboard_type", type.ordinal());
			program.setUniform("u_color", color);

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
