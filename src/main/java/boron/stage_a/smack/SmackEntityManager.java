package boron.stage_a.smack;

import boron.stage_a.base.collisionbox.CollisionBoxManager;
import boron.stage_a.base.collisionbox.collider.BoxCollider;

import boron.bstone.livingentity.LivingEntity;
import boron.bstone.livingentity.LivingEntityManager;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntityManager extends LivingEntityManager
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
	public void onLivingCreate(LivingEntity living)
	{
		super.onLivingCreate(living);

		if (living instanceof BoxCollider)
		{
			this.boundingManager.addCollider((BoxCollider) living);
		}
	}

	@Override
	public void onLivingDestroy(LivingEntity living)
	{
		super.onLivingDestroy(living);

		if (living instanceof BoxCollider)
		{
			this.boundingManager.removeCollider((BoxCollider) living);
		}
	}

	public CollisionBoxManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
