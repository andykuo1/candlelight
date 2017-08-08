package org.zilar.instance;

import org.bstone.render.Renderable;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform;
import org.joml.Matrix4f;

/**
 * Created by Andy on 4/8/17.
 */
public final class Instance implements Renderable
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
		transform.getTransformation(this.transformation);
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

	@Override
	public final Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		return dst.set(this.transformation);
	}

	@Override
	public Model getRenderModel()
	{
		return this.model;
	}

	@Override
	public boolean isRenderVisible()
	{
		return this.visible;
	}

	public final boolean isDead()
	{
		return this.dead;
	}
}
