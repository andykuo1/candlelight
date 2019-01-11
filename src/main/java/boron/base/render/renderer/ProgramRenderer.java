package boron.base.render.renderer;

import boron.base.render.material.Material;

import boron.bstone.mogli.Mesh;
import boron.bstone.mogli.Program;
import org.joml.Matrix4fc;

/**
 * Created by Andy on 8/10/17.
 */
public abstract class ProgramRenderer implements AutoCloseable
{
	protected final Program program;
	protected final Material material;

	public ProgramRenderer(Program program, Material material)
	{
		this.program = program;
		this.material = material;
	}

	@Override
	public void close() throws Exception
	{
		this.program.close();
	}

	public abstract void bind(Matrix4fc view, Matrix4fc projection);

	public abstract void unbind();

	public abstract void draw(Mesh mesh, Material material, Matrix4fc transformation);

	public Material getDefaultMaterial()
	{
		return this.material;
	}

	public Program getProgram()
	{
		return this.program;
	}
}
