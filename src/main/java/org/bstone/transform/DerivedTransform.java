package org.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/19/17.
 */
public class DerivedTransform implements Transform
{
	private final Transform parent;

	private Vector3f offset = new Vector3f();

	public DerivedTransform(Transform parent)
	{
		this.parent = parent;
	}

	@Override
	public Vector3f getForward(Vector3f dst)
	{
		return this.parent.getForward(dst);
	}

	@Override
	public Vector3f getUp(Vector3f dst)
	{
		return this.parent.getUp(dst);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.parent.getRight(dst);
	}

	@Override
	public Vector3f getPosition(Vector3f dst)
	{
		return this.parent.getPosition(dst).add(this.offset);
	}

	@Override
	public Quaternionf getRotation(Quaternionf dst)
	{
		return this.parent.getRotation(dst);
	}

	@Override
	public Vector3f getScale(Vector3f dst)
	{
		return this.parent.getScale(dst);
	}

	public Vector3fc offset()
	{
		return this.offset;
	}

	public Transform parent()
	{
		return this.parent;
	}

	public DerivedTransform setOffset(float x, float y, float z)
	{
		this.offset.set(x, y, z);
		return this;
	}
}
