package net.jimboi.glim.entity;

import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityGlim
{
	static EntityManager MANAGER;

	public static void setEntityManager(EntityManager entityManager)
	{
		MANAGER = entityManager;
	}
}
