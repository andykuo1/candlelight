package canary.cuplet.scene_main.component;

import canary.bstone.gameobject.GameObjectManager;

/**
 * Created by Andy on 9/3/17.
 */
public abstract class SystemLiving extends System
{
	protected final GameObjectManager gameObjectManager;

	public SystemLiving(GameObjectManager gameObjectManager)
	{
		super(gameObjectManager.getEntityManager());

		this.gameObjectManager = gameObjectManager;
	}
}
