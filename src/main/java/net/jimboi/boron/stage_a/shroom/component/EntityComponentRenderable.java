package net.jimboi.boron.stage_a.shroom.component;

import org.bstone.transform.Transform;
import org.joml.Matrix4f;
import org.qsilver.render.Renderable;
import org.zilar.entity.EntityComponent;
import org.zilar.model.Model;

/**
 * Created by Andy on 7/18/17.
 */
public class EntityComponentRenderable implements EntityComponent, Renderable
{
	public final Transform transform;
	public final Model model;
	private boolean visible;

	public EntityComponentRenderable(Transform transform, Model model)
	{
		this.transform = transform;
		this.model = model;
		this.visible = true;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
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
