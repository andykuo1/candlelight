package apricot.stage_b.glim.entity;

import apricot.base.entity.EntityManager;
import apricot.stage_b.glim.SceneGlim;

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
