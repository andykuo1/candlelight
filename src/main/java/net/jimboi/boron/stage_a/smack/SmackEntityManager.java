package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntityManager;

import org.zilar.entity.Entity;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntityManager extends LivingEntityManager<LivingEntity>
{
	private final CollisionBoxManager boundingManager;

	public SmackEntityManager()
	{
		 this.boundingManager = new CollisionBoxManager();
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

	public CollisionBoxManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
