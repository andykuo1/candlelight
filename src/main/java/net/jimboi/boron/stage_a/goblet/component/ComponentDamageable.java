package net.jimboi.boron.stage_a.goblet.component;

import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.entity.Component;

/**
 * Created by Andy on 8/13/17.
 */
public class ComponentDamageable implements Component
{
	public int maxDamage;
	public int damage;
	public int prevDamage;

	public boolean damaging;

	public final TickCounter damageTicks = new TickCounter(100);

	public ComponentDamageable(int maxDamage)
	{
		this.maxDamage = maxDamage;
		this.damage = 0;
		this.prevDamage = 0;
	}

	public void updateDamage()
	{

	}

	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	public int getDamage()
	{
		return this.damage;
	}
}
