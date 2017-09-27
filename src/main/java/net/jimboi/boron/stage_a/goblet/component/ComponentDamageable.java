package net.jimboi.boron.stage_a.goblet.component;

import net.jimboi.boron.stage_a.goblet.entity.IDamageSource;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.entity.Component;
import org.bstone.util.Procedure;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by Andy on 8/13/17.
 */
public class ComponentDamageable implements Component
{
	public Procedure onDamageTick = () -> {};
	public BiConsumer<IDamageSource, Integer> onDamageTaken = (damageSource, amount) -> {};
	public Procedure onHealthDepleted = () -> {};
	public Predicate<IDamageSource> canTakeDamageFrom = (damageSource) -> true;

	protected int maxHealth;
	protected int health;

	protected final TickCounter damageTicks = new TickCounter(30, true);

	public ComponentDamageable setMaxHealth(int health)
	{
		this.maxHealth = health;
		return this;
	}

	public ComponentDamageable setHealth(int health)
	{
		this.health = health;
		return this;
	}

	public void doDamageTick()
	{
		this.damageTicks.tick();
		this.onDamageTick.apply();
	}

	public void damage(IDamageSource damageSource, int amount)
	{
		if (this.canTakeDamageFrom.test(damageSource))
		{
			this.health -= amount;
			this.damageTicks.reset();

			this.onDamageTaken.accept(damageSource, amount);

			if (this.health <= 0)
			{
				this.onHealthDepleted.apply();
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

	public TickCounter getDamageTicks()
	{
		return this.damageTicks;
	}

	public boolean isBeingDamaged()
	{
		return !this.damageTicks.isComplete();
	}
}
