package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntityManager;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxCollider;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxManager;

import org.zilar.entity.Entity;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntityManager extends LivingEntityManager
{
	private final BoundingBoxManager boundingManager;

	public SmackEntityManager()
	{
		 this.boundingManager = new BoundingBoxManager();
	}

	@Override
	public void update()
	{
		super.update();

		this.boundingManager.update();
	}

	@Override
	protected void onLivingEntityCreate(LivingEntity living, Entity entity)
	{
		super.onLivingEntityCreate(living, entity);

		if (living instanceof BoundingBoxCollider)
		{
			this.boundingManager.add((BoundingBoxCollider) living);
		}
	}

	@Override
	protected void onLivingEntityDestroy(LivingEntity living, Entity entity)
	{
		super.onLivingEntityDestroy(living, entity);

		if (living instanceof BoundingBoxCollider)
		{
			this.boundingManager.remove((BoundingBoxCollider) living);
		}
	}

	public BoundingBoxManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
