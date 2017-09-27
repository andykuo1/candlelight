package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.component.ComponentDamageable;
import net.jimboi.boron.stage_a.goblet.component.ComponentMotion;
import net.jimboi.boron.stage_a.goblet.entity.base.EntitySolid;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.transform.Transform3;
import org.joml.Vector3f;

/**
 * Created by Andy on 8/11/17.
 */
public class EntitySlash extends EntitySolid implements IDamageSource
{
	protected EntityPlayer owner;

	protected final TickCounter ageTicks = new TickCounter(5);

	public EntitySlash(GobletWorld world, Transform3 transform, EntityPlayer owner)
	{
		super(world, transform,
				world.createBoundingBox(transform, 1),
				world.createRenderable2D(transform, '/', 0xFF0000));

		this.owner = owner;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		this.ageTicks.tick();

		this.getRenderable().getRenderModel().transformation().translation(-Math.abs(0.5F - this.ageTicks.getProgress()) * 0.5F, 0.5F - this.ageTicks.getProgress(), 0);

		if (this.ageTicks.isComplete())
		{
			this.setDead();
		}
		else if (this.ageTicks.getTicks() == this.ageTicks.getMaxTicks() / 2)
		{
			for(BoxCollider collider : this.world.getBoundingManager().getNearestColliders(this.transform.posX(), this.transform.posY(), 0.5F))
			{
				if (collider == this.owner) continue;

				if (collider instanceof EntityHurtable)
				{
					EntityHurtable hurtable = (EntityHurtable) collider;
					hurtable.getComponent(ComponentDamageable.class).damage(this, 0);
					Vector3f vec = this.owner.getTransform().position.sub(hurtable.getTransform().position3(), new Vector3f()).normalize().mul(0.5F).negate();
					hurtable.getTransform().translate(vec.x(), vec.y(), 0);
				}
				else if (collider instanceof EntityThrowable)
				{
					((EntityThrowable) collider).getComponent(ComponentMotion.class).addMotionTowards(this.owner.getTransform(), ((EntityThrowable) collider).getTransform(), 0.2F);
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

	@Override
	public boolean isColliderActive()
	{
		return true;
	}
}
