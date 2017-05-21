package org.qsilver.instance;

import org.qsilver.model.Material;
import org.qsilver.model.Model;
import net.jimboi.mod.transform.Transform;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/**
 * Created by Andy on 4/8/17.
 */
public final class Instance
{
	private final Model model;
	private final Material material;

	private final Matrix4f transformation = new Matrix4f();
	private final Matrix4f offset = new Matrix4f();

	private boolean visible;
	private boolean dead = false;

	public Instance(Model model, Material material)
	{
		this.model = model;
		this.material = material;

		this.visible = true;
	}

	public boolean onCreate()
	{
		return true;
	}

	public void onUpdate()
	{
	}

	public void onDestroy()
	{
	}

	public final Matrix4f offset()
	{
		return this.offset;
	}

	public final Instance setTransformation(Transform transform)
	{
		this.transformation.set(transform.transformation()).mul(this.offset);
		return this;
	}

	public final Instance setTransformation(Matrix4fc transformation)
	{
		this.transformation.set(transformation).mul(this.offset);
		return this;
	}

	public final void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public final void setDead()
	{
		this.dead = true;
	}

	public final Matrix4fc getTransformation()
	{
		return this.transformation;
	}

	public final Model getModel()
	{
		return this.model;
	}

	public final Material getMaterial()
	{
		return this.material;
	}

	public final boolean isVisible()
	{
		return this.visible;
	}

	public final boolean isDead()
	{
		return this.dead;
	}
}
