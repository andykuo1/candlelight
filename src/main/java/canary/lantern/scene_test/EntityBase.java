package canary.lantern.scene_test;

import canary.bstone.gameobject.GameObject;
import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 10/20/17.
 */
public abstract class EntityBase extends GameObject
{
	protected final Transform3 transform;

	public EntityBase(Transform3 transform)
	{
		this.transform = transform;
	}

	public Transform3 getTransform()
	{
		return this.transform;
	}
}
