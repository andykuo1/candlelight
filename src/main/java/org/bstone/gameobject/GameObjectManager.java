package org.bstone.gameobject;

import org.bstone.entity.EntityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Andy on 12/2/17.
 */
public class GameObjectManager
{
	private final EntityManager entityManager;

	private final List<GameObject> createQueue = new ArrayList<>();
	private final List<GameObject> createCache = new ArrayList<>();
	private final Set<GameObject> gameObjects = new HashSet<>();
	private volatile boolean cached = false;

	public GameObjectManager()
	{
		this.entityManager = new EntityManager();
	}

	public <T extends GameObject> T addGameObject(T gameObject)
	{
		if (this.cached)
		{
			this.createCache.add(gameObject);
		}
		else
		{
			this.createQueue.add(gameObject);
		}
		return gameObject;
	}

	public <T extends GameObject> T removeGameObject(T gameObject)
	{
		if (!gameObject.isDead())
		{
			gameObject.setDead();
			return gameObject;
		}
		return null;
	}

	public void update()
	{
		this.flushCreatedObjects();

		for(GameObject gameObject : this.gameObjects)
		{
			if (!gameObject.isDead())
			{
				gameObject.onUpdate();
			}
		}

		Iterator<GameObject> iter = this.gameObjects.iterator();
		while (iter.hasNext())
		{
			GameObject gameObject = iter.next();
			if (!gameObject.isDead())
			{
				gameObject.onLateUpdate();
			}
			else
			{
				gameObject.onDestroy();
				iter.remove();
				this.onGameObjectDestroy(gameObject);
				this.entityManager.removeEntity(gameObject);

				gameObject.gameObjectManager = null;
			}
		}
	}

	public void flushCreatedObjects()
	{
		while(!this.createQueue.isEmpty())
		{
			this.cached = true;

			for(GameObject gameObject : this.createQueue)
			{
				gameObject.gameObjectManager = this;
				gameObject.dead = false;

				this.entityManager.addEntity(gameObject);
				this.gameObjects.add(gameObject);
				gameObject.onCreate(this);
				this.onGameObjectCreate(gameObject);
			}
			this.createQueue.clear();

			this.cached = false;

			this.createQueue.addAll(this.createCache);
			this.createCache.clear();
		}
	}

	public void flushDestroyedObjects()
	{
		Iterator<GameObject> objects = this.gameObjects.iterator();
		while(objects.hasNext())
		{
			GameObject object = objects.next();
			if (object.isDead())
			{
				object.onDestroy();
				this.onGameObjectDestroy(object);
				objects.remove();
			}
		}
	}

	public void clear()
	{
		this.gameObjects.clear();
		this.entityManager.clear();
	}

	public Iterable<GameObject> getGameObjects()
	{
		return this.gameObjects;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	protected void onGameObjectCreate(GameObject gameObject)
	{
	}

	protected void onGameObjectDestroy(GameObject gameObject)
	{
	}

	protected void onUpdate()
	{
	}

	protected void onLateUpdate()
	{
	}
}
