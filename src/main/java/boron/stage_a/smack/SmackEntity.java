package boron.stage_a.smack;

import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.basicobject.LivingEntityBase;
import boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import boron.stage_a.base.collisionbox.collider.BoxCollider;

import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class SmackEntity extends LivingEntityBase implements BoxCollider
{
	protected final SmackWorld world;
	protected final AxisAlignedBoundingBox boundingBox;

	protected int health;

	public SmackEntity(SmackWorld world, Transform3 transform, float size, ComponentRenderable renderable)
	{
		super(transform, renderable);

		this.world = world;

		this.boundingBox = size > 0 ? this.world.createBoundingBox(transform.position3().x(), transform.position3().y(), size, size) : null;
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
