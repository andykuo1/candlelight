package org.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/19/17.
 */
public class DerivedTransform2 extends Transform2
{
	private final Transform2c parent;

	public DerivedTransform2(Transform2c parent)
	{
		this.parent = parent;
	}

	@Override
	public Vector3f getPosition(Vector3f dst)
	{
		this.parent.getPosition(dst);
		dst.add(super.position3());
		return dst;
	}

	@Override
	public Quaternionf getRotation(Quaternionf dst)
	{
		this.parent.getRotation(dst);
		dst.mul(super.quaternion());
		return dst;
	}

	@Override
	public Vector3f getScale(Vector3f dst)
	{
		this.parent.getScale(dst);
		dst.mul(super.scale3());
		return dst;
	}

	@Override
	public Quaternionfc quaternion()
	{
		return this.getRotation(new Quaternionf());
	}

	@Override
	public Vector3fc position3()
	{
		return this.getPosition(new Vector3f());
	}

	@Override
	public Vector3fc scale3()
	{
		return this.getScale(new Vector3f());
	}

	@Override
	public float angles()
	{
		return this.parent.angles() + super.angles();
	}

	@Override
	public float radians()
	{
		return this.parent.radians() + super.radians();
	}

	@Override
	public Vector2fc position2()
	{
		return this.parent.position2().add(super.position2(), new Vector2f());
	}

	@Override
	public Vector2fc scale2()
	{
		return this.parent.scale2().add(super.scale2(), new Vector2f());
	}

	public float anglesOffset()
	{
		return super.angles();
	}

	public float radiansOffset()
	{
		return super.radians;
	}

	public Vector2fc positionOffset()
	{
		return super.position;
	}

	public Vector2fc scaleOffset()
	{
		return super.scale;
	}
}
