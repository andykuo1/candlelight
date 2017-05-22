package net.jimboi.mod3.blob.livings;

import net.jimboi.mod.transform.Transform2;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Living2 extends LivingBase
{
	public Living2(float x, float y)
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
