package net.jimboi.stage_b.glim.entity.component;

import org.joml.Matrix4f;
import org.qsilver.entity.EntityComponent;
import org.qsilver.renderer.Renderable;
import org.zilar.model.Model;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentRenderable extends EntityComponent implements Renderable
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
		return dst.set(this.transform.transformation());
	}

	@Override
	public boolean isVisible()
	{
		return this.visible;
	}
}
