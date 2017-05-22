package net.jimboi.dood.system;

import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 5/22/17.
 */
public abstract class EntitySystem
{
	protected final EntityManager entityManager;

	public EntitySystem(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
