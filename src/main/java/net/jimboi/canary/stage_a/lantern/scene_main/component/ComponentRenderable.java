package net.jimboi.canary.stage_a.lantern.scene_main.component;

import net.jimboi.canary.stage_a.cuplet.model.Model;
import net.jimboi.canary.stage_a.cuplet.scene_main.Renderable;

import org.bstone.entity.Component;
import org.bstone.transform.Transform3;
import org.joml.Matrix4f;

/**
 * Created by Andy on 11/3/17.
 */
public class ComponentRenderable implements Component, Renderable
{
	public final Transform3 transform;
	public final Model model;
	public boolean visible;

	public ComponentRenderable(Transform3 transform, Model model)
	{
		this.transform = transform;
		this.model = model;
		this.visible = true;
	}

	@Override
	public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
	{
		return this.transform.getTransformation(dst);
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
}
