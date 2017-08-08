package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.collisionbox.response.CollisionSolver;

import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/6/17.
 */
public class EntitySpawner extends SmackEntity
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

		CollisionSolver.checkCollision(this.getBoundingBox(),
				this.world.getSmacks().getBoundingManager().getColliders(),
				(other) -> other instanceof EntityBullet,
				(collision) ->
				{
					if (collision != null && collision.getCollider() instanceof EntityBullet)
					{
						((EntityBullet) collision.getCollider()).damage(new DamageSource(this), 1);
						this.damage(null, 1);
					}
					return false;
				}
		);
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
		this.world.spawn(new EntityZombie(this.world, transform));
	}
}