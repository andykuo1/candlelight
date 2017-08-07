package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.smack.aabb.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxCollider;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntity extends LivingEntity implements BoundingBoxCollider
{
	protected final SmackWorld world;
	protected final AxisAlignedBoundingBox boundingBox;

	protected int health;

	public SmackEntity(SmackWorld world, Transform3 transform, float size, EntityComponentRenderable renderable)
	{
		super(transform, renderable);

		this.world = world;

		this.boundingBox = size > 0 ? this.world.createBoundingBox(transform.position3().x(), transform.position3().y(), size, size) : null;
	}

	@Override
	public void onLateUpdate()
	{
		super.onLateUpdate();

		if (this.boundingBox != null)
		{
			this.boundingBox.setPosition(this.transform.position3().x(), this.transform.position3().y());
		}
	}

	protected void onDamageTaken(DamageSource source, int damage)
	{

	}

	public void damage(DamageSource source, int damage)
	{
		this.health -= damage;

		this.onDamageTaken(source, damage);

		if (this.health <= 0)
		{
			this.setDead();
		}
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public int getHealth()
	{
		return this.health;
	}

	public SmackEntity setPosition(float x, float y)
	{
		this.transform.position.set(x, y, 0);
		return this;
	}

	public SmackEntity setRotation(float radians)
	{
		this.transform.rotation.rotationZ(radians);
		return this;
	}

	public SmackEntity setScale(float width, float height)
	{
		this.transform.scale.set(width, height, 1);
		return this;
	}

	@Override
	public final AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}

	public final SmackWorld getWorld()
	{
		return this.world;
	}
}
