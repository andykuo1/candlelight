package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.render.material.Material;
import org.bstone.render.material.PropertyColor;
import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityHurtable extends EntityMotion implements IBurnable
{
	protected final int hurtColor;

	protected int maxHealth;
	protected int health;

	protected int maxDamageTicks = 30;
	protected int damageTicks;

	protected int maxFireTicks = 180 + 1;
	protected int fireTicks;

	protected int burnRate = 60;

	public EntityHurtable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, EntityComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);

		this.hurtColor = 0xFF0000;
	}

	@Override
	public boolean onCreate()
	{
		this.health = this.maxHealth;

		return super.onCreate();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.fireTicks > 0)
		{
			--this.fireTicks;

			if (this.fireTicks % this.burnRate == 0)
			{
				this.damage(null, 1);
			}
		}

		if (this.damageTicks > 0)
		{
			--this.damageTicks;
			Material material = this.getRenderable().getRenderModel().getMaterial();
			PropertyColor.PROPERTY.bind(material)
					.setColor(ColorUtil.getColorMix(this.mainColor, this.hurtColor, this.damageTicks / (float) this.maxDamageTicks))
					.unbind();
		}
	}

	@Override
	public boolean canSetFire(float x, float y, float strength)
	{
		return (int) (Math.floor(this.transform.posX()) - Math.floor(x)) == 0 &&
				(int) (Math.floor(this.transform.posY()) - Math.floor(y)) == 0;
	}

	@Override
	public void setOnFire(float strength)
	{
		this.fireTicks = this.maxFireTicks;
	}

	@Override
	public void extinguish()
	{
		this.fireTicks = 0;
	}

	@Override
	public boolean isOnFire()
	{
		return this.fireTicks > 0;
	}

	public void onDamageTaken(IDamageSource damageSource, int amount)
	{
	}

	public boolean canTakeDamageFrom(IDamageSource damageSource)
	{
		return true;
	}

	public void damage(IDamageSource damageSource, int amount)
	{
		if (this.canTakeDamageFrom(damageSource))
		{
			this.health -= amount;
			this.damageTicks = this.maxDamageTicks;

			this.onDamageTaken(damageSource, amount);

			if (this.health <= 0)
			{
				this.setDead();
			}
		}
	}

	public int getMaxHealth()
	{
		return this.maxHealth;
	}

	public int getHealth()
	{
		return this.health;
	}

	public boolean isBeingDamaged()
	{
		return this.damageTicks > 0;
	}
}
