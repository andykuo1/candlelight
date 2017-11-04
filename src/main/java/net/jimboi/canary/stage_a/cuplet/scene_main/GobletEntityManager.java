package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.canary.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.canary.stage_a.base.collisionbox.collider.BoxCollider;

import org.bstone.livingentity.LivingEntity;
import org.bstone.livingentity.LivingEntityManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntityManager extends LivingEntityManager
{
	private final CollisionBoxManager boundingManager;
	private Set<BoxCollider> colliders;

	public GobletEntityManager()
	{
		this.boundingManager = new CollisionBoxManager();
		this.colliders = new HashSet<>();
	}

	@Override
	public void update()
	{
		super.update();

		this.boundingManager.update(this.colliders);
	}

	@Override
	public void clear()
	{
		super.clear();

		this.colliders.clear();
	}

	@Override
	public void onLivingCreate(LivingEntity living)
	{
		super.onLivingCreate(living);

		if (living instanceof BoxCollider)
		{
			this.colliders.add((BoxCollider) living);
		}
	}

	@Override
	public void onLivingDestroy(LivingEntity living)
	{
		super.onLivingDestroy(living);

		if (living instanceof BoxCollider)
		{
			this.colliders.remove(living);
		}
	}

	public CollisionBoxManager getBoundingManager()
	{
		return this.boundingManager;
	}

	public Set<BoxCollider> getColliders()
	{
		return this.colliders;
	}
}
