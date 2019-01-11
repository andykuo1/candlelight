package canary.cuplet.scene_main;

import canary.base.collisionbox.CollisionBoxManager;
import canary.base.collisionbox.collider.BoxCollider;

import canary.bstone.gameobject.GameObject;
import canary.bstone.gameobject.GameObjectManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntityManager extends GameObjectManager
{
	private final CollisionBoxManager boundingManager;
	private Set<BoxCollider> colliders;

	public GobletEntityManager()
	{
		this.boundingManager = new CollisionBoxManager();
		this.colliders = new HashSet<>();
	}

	@Override
	public void update()
	{
		super.update();

		this.boundingManager.update(this.colliders);
	}

	@Override
	public void clear()
	{
		super.clear();

		this.colliders.clear();
	}

	@Override
	protected void onGameObjectCreate(GameObject gameObject)
	{
		super.onGameObjectCreate(gameObject);

		if (gameObject instanceof BoxCollider)
		{
			this.colliders.add((BoxCollider) gameObject);
		}
	}

	@Override
	protected void onGameObjectDestroy(GameObject gameObject)
	{
		super.onGameObjectDestroy(gameObject);

		if (gameObject instanceof BoxCollider)
		{
			this.colliders.remove(gameObject);
		}
	}

	public CollisionBoxManager getBoundingManager()
	{
		return this.boundingManager;
	}

	public Set<BoxCollider> getColliders()
	{
		return this.colliders;
	}
}
