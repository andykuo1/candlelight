package net.jimboi.apricot.stage_a.mod.model;

import org.bstone.mogli.Mesh;
import org.joml.Matrix4f;

/**
 * Created by Andy on 4/29/17.
 */
public class Model
{
	//Animations
	//Bones
	//Etc.
	private final Matrix4f transformation = new Matrix4f();
	private final Mesh mesh;

	public Model(Mesh mesh)
	{
		this.mesh = mesh;
	}

	public Matrix4f transformation()
	{
		return this.transformation;
	}

	public Mesh getMesh()
	{
		return this.mesh;
	}

	public void bind()
	{
		this.mesh.bind();
	}

	public void unbind()
	{
		this.mesh.unbind();
	}
}
