package org.bstone.render.renderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.render.material.Material;
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
	public void close()
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
}
