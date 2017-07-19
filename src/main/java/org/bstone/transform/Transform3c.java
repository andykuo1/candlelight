package org.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class Transform3c implements Transform
{
	public static final Vector3fc XAXIS = new Vector3f(1, 0, 0);
	public static final Vector3fc YAXIS = new Vector3f(0, 1, 0);
	public static final Vector3fc ZAXIS = new Vector3f(0, 0, 1);

	public static final Vector3fc IDENTITY = new Vector3f(1, 1, 1);
	public static final Vector3fc ZERO = new Vector3f(0, 0, 0);

	protected final Vector3f vec3 = new Vector3f();
	protected final Quaternionf quat = new Quaternionf();

	@Override
	public Vector3f getForward(Vector3f dst)
	{
		return this.getRotation(this.quat).normalizedPositiveZ(dst);
	}

	@Override
	public final Vector3f getRight(Vector3f dst)
	{
		return this.getRotation(this.quat).normalizedPositiveX(dst);
	}

	@Override
	public final Vector3f getUp(Vector3f dst)
	{
		return this.getRotation(this.quat).normalizedPositiveY(dst);
	}

	protected final Vector3f eulerAngles = new Vector3f();
	protected final Vector3f eulerRadians = new Vector3f();

	public final Vector3fc eulerRadians()
	{
		return this.getRotation(this.quat).getEulerAnglesXYZ(this.eulerRadians);
	}

	public final Vector3fc eulerAngles()
	{
		return this.getRotation(this.quat).getEulerAnglesXYZ(this.eulerAngles).mul(RAD2DEG);
	}

	public abstract Quaternionfc quaternion();

	public abstract Vector3fc position3();
	public abstract Vector3fc scale3();
}
