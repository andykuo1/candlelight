package org.bstone.render.renderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 8/5/17.
 */
public class WireframeProgramRenderer  implements AutoCloseable
{
	protected final Program program;

	private final Matrix4f viewProjection = new Matrix4f();
	private final Matrix4f modelViewProjection = new Matrix4f();

	public WireframeProgramRenderer()
	{
		Shader vsh = new Shader(new ResourceLocation("base:wireframe.vsh"), GL20.GL_VERTEX_SHADER);
		Shader fsh = new Shader(new ResourceLocation("base:wireframe.fsh"), GL20.GL_FRAGMENT_SHADER);
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

		this.viewProjection.set(projection).mul(view);
	}

	public void unbind()
	{
		final Program program = this.program;
		program.unbind();
	}

	public void draw(Mesh mesh, Vector4fc color, Matrix4fc transformation)
	{
		final Program program = this.program;
		program.setUniform("u_model_view_projection", this.viewProjection.mul(transformation, this.modelViewProjection));
		mesh.bind();
		{
			program.setUniform("u_color", color);
			GL11.glDrawElements(GL11.GL_LINE_LOOP, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		mesh.unbind();
	}
}
