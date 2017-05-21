package net.jimboi.mod2;

import net.jimboi.mod.entity.Entity;
import net.jimboi.mod.transform.Transform2;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Entity2D extends Entity
{
	public Entity2D(float x, float y)
	{
		super(Transform2.create());

		this.transform().position.set(x, y, 0);
	}

	@Override
	public Transform2 transform()
	{
		return (Transform2) super.transform();
	}
}
