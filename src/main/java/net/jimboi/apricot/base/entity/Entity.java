package net.jimboi.apricot.base.entity;

import net.jimboi.apricot.base.component.ManifestEntity;

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
	public Entity addComponent(EntityComponent component)
	{
		return (Entity) super.addComponent(component);
	}

	@Override
	public <T extends EntityComponent> T removeComponent(Class<T> componentType)
	{
		return super.removeComponent(componentType);
	}
}
