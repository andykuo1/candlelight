package net.jimboi.apricot.stage_a.blob.livings;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 5/19/17.
 */
public abstract class Living3 extends LivingBase
{
	public Living3(float x, float y, float z)
	{
		super(new Transform3());

		this.transform().position.set(x, y, z);
	}

	@Override
	public Transform3 transform()
	{
		return (Transform3) super.transform();
	}
}
