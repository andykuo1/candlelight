package net.jimboi.boron.stage_a.goblet.component;

import org.bstone.entity.Component;
import org.bstone.entity.EntityManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Andy on 8/12/17.
 */
public class SystemBehavior
{
	protected final EntityManager entityManager;
	protected final Collection<Component> components = new HashSet<>();

	public SystemBehavior(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void update()
	{
		this.entityManager.getComponents(ComponentBehavior.class, this.components);

		for(Component component : this.components)
		{
			this.update((ComponentBehavior) component);
		}
	}

	public void update(ComponentBehavior componentBehavior)
	{

	}
}
