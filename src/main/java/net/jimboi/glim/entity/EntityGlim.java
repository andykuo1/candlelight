package net.jimboi.glim.entity;

import net.jimboi.glim.gameentity.GameEntityManager;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityGlim
{
	static GameEntityManager MANAGER;

	public static void setEntityManager(GameEntityManager entityManager)
	{
		MANAGER = entityManager;
	}
}
