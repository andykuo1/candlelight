package net.jimboi.stage_b.glim.entity.component;

import org.bstone.transform.Transform3;
import org.joml.Matrix4f;
import org.qsilver.render.Renderable;
import org.zilar.entity.EntityComponent;
import org.zilar.model.Model;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentRenderable implements EntityComponent, Renderable
{
	public final Transform3 transform;
	public final Model model;
	public boolean visible;

	public EntityComponentRenderable(Transform3 transform, Model model)
	{
		this.transform = transform;
		this.model = model;
		this.visible = true;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public Transform3 getTransform()
	{
		return this.transform;
	}

	@Override
	public Model getModel()
	{
		return this.model;
	}

	@Override
	public Matrix4f getRenderTransformation(Matrix4f dst)
	{
		return this.transform.getTransformation(dst);
	}

	@Override
	public boolean isVisible()
	{
		return this.visible;
	}
}
