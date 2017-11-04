package net.jimboi.canary.stage_a.base.renderer;

import org.bstone.asset.Asset;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4fc;

/**
 * Created by Andy on 10/31/17.
 */
public abstract class AbstractRenderer
{
	protected final Asset<Program> program;

	public AbstractRenderer(Asset<Program> program)
	{
		this.program = program;
	}

	public abstract void bind(Matrix4fc view, Matrix4fc projection);

	public abstract void unbind();

	public abstract void draw(Asset<Mesh> mesh, Material material, Matrix4fc transformation);

	public Asset<Program> getProgram()
	{
		return this.program;
	}
}
