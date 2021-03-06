package boron.stage_a.base.basicobject;

import boron.base.render.Renderable;
import boron.base.render.model.Model;

import boron.bstone.entity.Component;
import boron.bstone.transform.Transform;
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
