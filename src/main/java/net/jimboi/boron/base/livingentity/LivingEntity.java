package net.jimboi.boron.base.livingentity;

import org.bstone.living.Living;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/23/17.
 */
public class LivingEntity extends Living implements EntityComponent
{
	private final Transform3 transform;
	private final EntityComponentRenderable renderable;

	public LivingEntity(Transform3 transform, EntityComponentRenderable renderable)
	{
		this.transform = transform;
		this.renderable = renderable;
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	public void onEntityCreate(Entity entity)
	{
	}

	public void onEntityDestroy(Entity entity)
	{
	}

	public Transform3c getTransform()
	{
		return this.transform;
	}

	public EntityComponentRenderable getRenderable()
	{
		return this.renderable;
	}
}
