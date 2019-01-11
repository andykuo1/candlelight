package canary.base;

import canary.bstone.asset.Asset;
import canary.bstone.material.Material;
import canary.bstone.mogli.Mesh;
import org.joml.Matrix4f;

/**
 * Created by Andy on 10/31/17.
 */
public class Model
{
	protected Asset<Mesh> mesh;
	protected Material material;

	protected final Matrix4f transformation = new Matrix4f();

	public Model(Asset<Mesh> mesh)
	{
		this(mesh, new Material());
	}

	public Model(Asset<Mesh> mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
	}

	public Material material()
	{
		return this.material;
	}

	public Matrix4f transformation()
	{
		return this.transformation;
	}
}
