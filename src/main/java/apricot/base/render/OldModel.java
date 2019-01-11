package apricot.base.render;

import apricot.base.material.OldMaterial;
import apricot.base.asset.Asset;

import apricot.bstone.mogli.Mesh;
import org.joml.Matrix4f;

/**
 * Created by Andy on 6/13/17.
 */
@Deprecated
public class OldModel
{
	protected final Asset<Mesh> mesh;
	protected final Matrix4f transformation = new Matrix4f();
	protected final OldMaterial material;
	protected final String renderType;

	public OldModel(Asset<Mesh> mesh, OldMaterial material, String renderType)
	{
		this.mesh = mesh;
		this.material = material;
		this.renderType = renderType;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
	}

	public OldMaterial getMaterial()
	{
		return this.material;
	}

	public String getRenderType()
	{
		return this.renderType;
	}

	public Matrix4f transformation()
	{
		return this.transformation;
	}
}
