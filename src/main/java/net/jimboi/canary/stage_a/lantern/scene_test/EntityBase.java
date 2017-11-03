package net.jimboi.canary.stage_a.lantern.scene_test;

import org.bstone.entity.EntityManager;
import org.bstone.living.LivingManager;
import org.bstone.livingentity.LivingEntity;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 10/20/17.
 */
public abstract class EntityBase extends LivingEntity
{
	protected final Transform3 transform;

	public EntityBase(Transform3 transform)
	{
		this.transform = transform;
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{

	}

	@Override
	public void onLivingCreate(LivingManager livingManager)
	{

	}

	@Override
	public void onLivingUpdate()
	{

	}

	@Override
	public void onLivingLateUpdate()
	{

	}

	@Override
	public void onLivingDestroy()
	{

	}

	@Override
	public void onEntityDestroy()
	{

	}

	public Transform3 getTransform()
	{
		return this.transform;
	}
}
