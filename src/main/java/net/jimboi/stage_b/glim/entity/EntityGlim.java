package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.SceneGlim;

import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityGlim
{
	static EntityManager MANAGER;
	static SceneGlim SCENE;

	public static void setup(SceneGlim scene, EntityManager entityManager)
	{
		SCENE = scene;
		MANAGER = entityManager;
	}
}
