package org.bstone.render.model;

import org.bstone.mogli.Mesh;
import org.bstone.render.material.Material;
import org.joml.Matrix4f;
import org.qsilver.asset.Asset;

/**
 * Created by Andy on 8/10/17.
 */
public class Model
{
	protected final Asset<Mesh> mesh;
	protected final Matrix4f transformation = new Matrix4f();
	protected final Material material;

	public Model(Asset<Mesh> mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
	}

	public Material getMaterial()
	{
		return this.material;
	}

	public Matrix4f transformation()
	{
		return this.transformation;
	}
}
