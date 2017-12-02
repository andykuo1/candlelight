package org.bstone.gameobject;

import org.bstone.entity.Entity;

/**
 * Created by Andy on 12/2/17.
 */
public class GameObject extends Entity
{
	GameObjectManager gameObjectManager;
	boolean dead = false;

	protected void onCreate(GameObjectManager gameObjectManager)
	{}

	protected void onUpdate()
	{}

	protected void onLateUpdate()
	{}

	protected void onDestroy()
	{}

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}

	public final GameObjectManager getGameObjectManager()
	{
		return this.gameObjectManager;
	}
}
