package boron.stage_a.goblet;

import boron.stage_a.base.collisionbox.CollisionBoxManager;
import boron.stage_a.base.collisionbox.collider.BoxCollider;

import boron.bstone.livingentity.LivingEntity;
import boron.bstone.livingentity.LivingEntityManager;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntityManager extends LivingEntityManager
{
	private final CollisionBoxManager boundingManager;

	public GobletEntityManager()
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
	public void clear()
	{
		super.clear();

		this.boundingManager.clear();
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
