package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntityManager;
import net.jimboi.boron.stage_a.smack.aabb.BoxCollider;
import net.jimboi.boron.stage_a.smack.aabb.BoxCollisionManager;

import org.zilar.entity.Entity;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntityManager extends LivingEntityManager
{
	private final BoxCollisionManager boundingManager;

	public SmackEntityManager()
	{
		 this.boundingManager = new BoxCollisionManager();
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

		if (living instanceof BoxCollider)
		{
			this.boundingManager.add((BoxCollider) living);
		}
	}

	@Override
	protected void onLivingEntityDestroy(LivingEntity living, Entity entity)
	{
		super.onLivingEntityDestroy(living, entity);

		if (living instanceof BoxCollider)
		{
			this.boundingManager.remove((BoxCollider) living);
		}
	}

	public BoxCollisionManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
