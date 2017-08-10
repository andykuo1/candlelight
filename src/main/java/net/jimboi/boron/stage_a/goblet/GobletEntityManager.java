package net.jimboi.boron.stage_a.goblet;

import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntityManager;

import org.zilar.entity.Entity;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntityManager extends LivingEntityManager<GobletEntity>
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
	protected void onLivingEntityCreate(GobletEntity living, Entity entity)
	{
		super.onLivingEntityCreate(living, entity);

		if (living instanceof BoxCollider)
		{
			this.boundingManager.add((BoxCollider) living);
		}
	}

	@Override
	protected void onLivingEntityDestroy(GobletEntity living, Entity entity)
	{
		super.onLivingEntityDestroy(living, entity);

		if (living instanceof BoxCollider)
		{
			this.boundingManager.remove((BoxCollider) living);
		}
	}

	public CollisionBoxManager getBoundings()
	{
		return this.boundingManager;
	}
}
