package net.jimboi.stage_b.gnome.instance;

import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.transform.Transform;

import org.joml.Matrix4f;

/**
 * Created by Andy on 4/8/17.
 */
public final class Instance
{
	private final Model model;

	private final Matrix4f transformation = new Matrix4f();

	private boolean visible;
	private boolean dead = false;

	public Instance(Model model)
	{
		this.model = model;

		this.visible = true;
	}

	public final Instance setTransformation(Transform transform)
	{
		this.transformation.set(transform.transformation());
		return this;
	}

	public final Matrix4f transformation()
	{
		return this.transformation;
	}

	public final void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public final void setDead()
	{
		this.dead = true;
	}

	public final Matrix4f getRenderTransformation(Matrix4f dst)
	{
		return dst.set(this.transformation).mul(this.model.transformation());
	}

	public final Model getModel()
	{
		return this.model;
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
