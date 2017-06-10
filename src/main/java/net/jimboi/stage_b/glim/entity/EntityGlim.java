package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.gameentity.GameEntityManager;

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
