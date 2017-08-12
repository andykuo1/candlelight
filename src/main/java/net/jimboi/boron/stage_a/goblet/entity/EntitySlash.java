package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/11/17.
 */
public class EntitySlash extends EntitySolid implements ActiveBoxCollider, IDamageSource
{
	private int maxAge = 5;
	private int age;

	public EntitySlash(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 1),
				world.createRenderable2D(transform, '/', 0xFF0000));
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		++this.age;
		this.getRenderable().getRenderModel().transformation().translation(-Math.abs(0.5F - (this.age / (float) this.maxAge)) * 0.5F, 0.5F - (this.age / (float) this.maxAge), 0);
		if (this.age > this.maxAge)
		{
			this.setDead();
		}
		else if (this.age == this.maxAge / 2)
		{
			for(BoxCollider collider : this.world.getBoundingManager().getNearestColliders(this.transform.posX(), this.transform.posY(), 0.5F))
			{
				if (collider instanceof EntityHurtable)
				{
					EntityHurtable hurtable = (EntityHurtable) collider;
					hurtable.damage(this, 1);
				}
			}
		}
	}

	@Override
	public void onPreCollisionUpdate()
	{

	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		return false;
	}

	@Override
	public void onPostCollisionUpdate()
	{

	}

	@Override
	public boolean canCollideWith(BoxCollider collider)
	{
		return false;
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}
}
