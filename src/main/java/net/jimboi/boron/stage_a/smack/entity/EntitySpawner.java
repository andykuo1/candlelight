package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxActiveCollider;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxCollider;
import net.jimboi.boron.stage_a.smack.aabb.IntersectionData;

import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

import java.util.List;

/**
 * Created by Andy on 8/6/17.
 */
public class EntitySpawner extends SmackEntity implements BoundingBoxActiveCollider
{
	protected float hurt;
	protected int color;

	private int maxCooldown;
	private int minCooldown;
	private int cooldown;

	public EntitySpawner(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 0.5F, world.createRenderable2D(transform, '$', 0x00FF00));

		this.health = 10;

		this.maxCooldown = 300;
		this.minCooldown = 100;
		this.cooldown = this.maxCooldown;

		this.color = ColorUtil.getColor(this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class).getColor());
	}

	@Override
	public void onUpdate()
	{
		if (this.hurt > 0)
		{
			this.hurt -= 0.2F;
			PropertyColor propertyColor = this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class);
			propertyColor.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt));
		}

		super.onUpdate();

		--this.cooldown;
		if (this.cooldown <= 0)
		{
			this.spawn();

			this.cooldown = this.world.getRandom().nextInt(this.maxCooldown - this.minCooldown) + this.minCooldown;
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for(int i = 8; i > 0; --i)
		{
			this.fireMeat();
		}
	}


	@Override
	public void onCheckIntersection(List<IntersectionData> intersections)
	{
		if (!intersections.isEmpty())
		{
			SmackEntity other = (SmackEntity) intersections.get(0).getCollider();

			if (other instanceof EntityBullet)
			{
				other.damage(new DamageSource(this), 1);

				this.damage(null, 1);
			}
		}
	}

	@Override
	public boolean canCollideWith(BoundingBoxCollider collider)
	{
		return collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityBullet);
	}

	@Override
	protected void onDamageTaken(DamageSource source, int damage)
	{
		super.onDamageTaken(source, damage);

		this.hurt += damage;

		if (this.hurt > 0)
		{
			PropertyColor propertyColor = this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class);
			propertyColor.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt));
		}
	}

	@Override
	public void damage(DamageSource source, int damage)
	{
		super.damage(source, damage);

		if (this.isDead())
		{
			for(int i = this.world.getRandom().nextInt(6); i > 0; --i)
			{
				Transform3 transform = new Transform3();
				transform.position.set(this.transform.position3());
				this.world.spawn(new EntityAmmo(this.world, transform));
			}
		}
	}

	public void fireMeat()
	{
		Transform3 transform = new Transform3();
		transform.position.set(this.transform.position3());

		this.world.spawn(new EntityMeat(this.world, transform, 0x00FF00));
	}

	protected void spawn()
	{
		Transform3 transform = new Transform3();
		transform.position.set(this.transform.position3());
		this.world.spawn(new EntityMonster(this.world, transform));
	}
}
