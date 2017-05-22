package net.jimboi.mod3.blob.livings;

import net.jimboi.mod.transform.Transform3;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Living3 extends LivingBase
{
	public Living3(float x, float y, float z)
	{
		super(Transform3.create());

		this.transform().position.set(x, y, z);
	}

	@Override
	public Transform3 transform()
	{
		return (Transform3) super.transform();
	}
}
