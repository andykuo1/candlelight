package net.jimboi.boron.stage_a.shroom.component;

import org.bstone.living.Living;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.zilar.bounding.BoundingCollider;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/17/17.
 */
public class LivingEntity extends Living implements EntityComponent
{
	private final Transform3 transform;
	private final EntityComponentRenderable renderable;

	private final BoundingCollider collider;

	public LivingEntity(Transform3 transform, BoundingCollider collider, EntityComponentRenderable renderable)
	{
		this.transform = transform;
		this.collider = collider;
		this.renderable = renderable;

		if (this.collider != null)
		{
			this.collider.update(this.transform.position.x(), this.transform.position.z());
		}
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	public void onEntityCreate(Entity entity)
	{
		entity.addComponent(new EntityComponentTransform(this.transform));
		if (this.collider != null)
		{
			entity.addComponent(new EntityComponentBounding(this.collider));
		}
		if (this.renderable != null)
		{
			entity.addComponent(this.renderable);
		}
	}

	public void onEntityDestroy(Entity entity)
	{
	}

	public Transform3c getTransform()
	{
		return this.transform;
	}

	public BoundingCollider getCollider()
	{
		return this.collider;
	}

	public EntityComponentRenderable getRenderable()
	{
		return this.renderable;
	}
}
