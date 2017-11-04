package net.jimboi.canary.stage_a.cuplet.basicobject;

import net.jimboi.canary.stage_a.base.model.Model;
import net.jimboi.canary.stage_a.cuplet.scene_main.Renderable;

import org.bstone.entity.Component;
import org.bstone.transform.Transform;
import org.joml.Matrix4f;

/**
 * Created by Andy on 8/12/17.
 */
public class ComponentRenderable implements Component, Renderable
{
	public Transform transform;
	public Model model;
	private boolean visible;

	public ComponentRenderable(Transform transform, Model model)
	{
		this.transform = transform;
		this.model = model;
		this.visible = true;
	}

	public void setTransform(Transform transform)
	{
		this.transform = transform;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public Model getRenderModel()
	{
		return this.model;
	}

	@Override
	public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		return this.transform.getTransformation(dst);
	}

	@Override
	public boolean isRenderVisible()
	{
		return this.visible;
	}
}
