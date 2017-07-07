package org.zilar.model;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.joml.Matrix4f;
import org.qsilver.asset.Asset;

/**
 * Created by Andy on 6/13/17.
 */
public class Model
{
	private final Asset<Mesh> mesh;
	private final Material material;
	private final String renderType;
	private final Matrix4f transformation = new Matrix4f();

	public Model(Asset<Mesh> mesh, Material material, String renderType)
	{
		this.mesh = mesh;
		this.material = material;
		this.renderType = renderType;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
	}

	public Material getMaterial()
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
