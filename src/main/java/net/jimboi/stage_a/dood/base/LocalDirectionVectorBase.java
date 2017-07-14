package net.jimboi.stage_a.dood.base;

import org.bstone.transform.DirectionVector3;
import org.bstone.transform.Transform3;
import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public class LocalDirectionVectorBase implements DirectionVector3
{
	private final Transform3 transform;

	public LocalDirectionVectorBase(Transform3 transform)
	{
		this.transform = transform;
	}

	@Override
	public Vector3f getForward(Vector3f dst)
	{
		return Transform3.YAXIS.cross(this.getRight(dst), dst);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.transform.getRight(dst);
	}

	@Override
	public Vector3f getUp(Vector3f dst)
	{
		return dst.set(Transform3.YAXIS);
	}
}
