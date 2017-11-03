package org.zilar.render.renderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.zilar.render.material.Material;
import org.zilar.render.material.PropertyColor;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 8/5/17.
 */
public class WireframeProgramRenderer extends ProgramRenderer
{
	private final Matrix4f viewProjection = new Matrix4f();
	private final Matrix4f modelViewProjection = new Matrix4f();
	private final Vector4f color = new Vector4f();

	public WireframeProgramRenderer()
	{
		super(new Program(), new Material());

		try (Shader vsh = new Shader(new ResourceLocation("base:wireframe.vsh"), GL20.GL_VERTEX_SHADER))
		{
			try (Shader fsh = new Shader(new ResourceLocation("base:wireframe.fsh"), GL20.GL_FRAGMENT_SHADER))
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

		this.material.addProperty(PropertyColor.PROPERTY);
	}

	@Override
	public void bind(Matrix4fc view, Matrix4fc projection)
	{
		final Program program = this.program;
		program.bind();

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		this.viewProjection.set(projection).mul(view);
	}

	@Override
	public void unbind()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		final Program program = this.program;
		program.unbind();
	}

	@Override
	public void draw(Mesh mesh, Material material, Matrix4fc transformation)
	{
		this.draw(mesh,
				PropertyColor.PROPERTY.getNormalizedRGB(material),
				transformation);
	}

	public void draw(Mesh mesh, Vector3fc color, Matrix4fc transformation)
	{
		final Program program = this.program;
		program.setUniform("u_model_view_projection", this.viewProjection.mul(transformation, this.modelViewProjection));
		mesh.bind();
		{
			program.setUniform("u_color", this.color.set(color.x(), color.y(), color.z(), 1));
			GL11.glDrawElements(GL11.GL_LINE_LOOP, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		mesh.unbind();
	}
}
