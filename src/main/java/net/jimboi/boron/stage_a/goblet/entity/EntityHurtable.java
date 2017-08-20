package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.living.LivingManager;
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

	protected final TickCounter damageTicks = new TickCounter(30, true);

	protected final TickCounter fireTicks = new TickCounter(180, true);

	protected int burnRate = 60;

	public EntityHurtable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);

		this.hurtColor = 0xFF0000;
	}

	@Override
	public void onLivingCreate(LivingManager livingManager)
	{
		this.health = this.maxHealth;
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.fireTicks.isComplete())
		{
			this.fireTicks.tick();

			if (this.fireTicks.getTicks() % this.burnRate == 0)
			{
				this.damage(null, 1);
			}
		}

		if (!this.damageTicks.isComplete())
		{
			this.damageTicks.tick();
			Material material = this.getRenderable().getRenderModel().getMaterial();
			PropertyColor.PROPERTY.bind(material)
					.setColor(ColorUtil.getColorMix(this.mainColor, this.hurtColor, 1 - this.damageTicks.getProgress()))
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
		this.fireTicks.reset();
		this.damage(null, 2);
	}

	@Override
	public void extinguish()
	{
		this.fireTicks.setComplete();
	}

	@Override
	public boolean isOnFire()
	{
		return !this.fireTicks.isComplete();
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
			this.damageTicks.reset();

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
		return !this.damageTicks.isComplete();
	}
}
