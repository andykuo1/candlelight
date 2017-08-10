package net.jimboi.apricot.stage_b.glim.entity.component;

import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.render.OldRenderable;

import org.bstone.transform.Transform3;
import org.joml.Matrix4f;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentRenderable implements EntityComponent, OldRenderable
{
	public final Transform3 transform;
	public final OldModel model;
	public boolean visible;

	public EntityComponentRenderable(Transform3 transform, OldModel model)
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
	public OldModel getRenderModel()
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
