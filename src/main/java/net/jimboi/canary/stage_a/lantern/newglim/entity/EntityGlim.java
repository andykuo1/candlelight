package net.jimboi.canary.stage_a.lantern.newglim.entity;

import net.jimboi.canary.stage_a.lantern.newglim.SceneGlim;

import org.zilar.entity.EntityManager;

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
