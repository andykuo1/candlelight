package net.jimboi.boron.stage_a.shroom.component;

import org.bstone.living.Living;
import org.bstone.transform.Transform;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/17/17.
 */
public class LivingEntity extends Living implements EntityComponent
{
	protected final Transform transform;
	protected final EntityComponentRenderable renderable;

	public LivingEntity(Transform transform, EntityComponentRenderable renderable)
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
		entity.addComponent(new EntityComponentTransform(this.transform));
		if (this.renderable != null)
		{
			entity.addComponent(this.renderable);
		}
	}

	public void onEntityDestroy(Entity entity)
	{
	}

	public Transform getTransform()
	{
		return this.transform;
	}

	public EntityComponentRenderable getRenderable()
	{
		return this.renderable;
	}
}
