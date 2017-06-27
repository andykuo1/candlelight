package org.qsilver.entity;

import org.bstone.component.ManifestEntity;

/**
 * Created by Andy on 6/25/17.
 */
public class Entity extends ManifestEntity<EntityComponent>
{
	private boolean dead = false;

	Entity() {}

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}

	@Override
	public ManifestEntity addComponent(EntityComponent component)
	{
		return super.addComponent(component);
	}

	@Override
	public <T extends EntityComponent> T removeComponent(Class<T> componentType)
	{
		return super.removeComponent(componentType);
	}
}
