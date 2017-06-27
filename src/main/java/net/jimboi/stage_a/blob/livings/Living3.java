package net.jimboi.stage_a.blob.livings;

import org.zilar.transform.Transform3;

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
