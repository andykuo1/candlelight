package org.zilar.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 4/30/17.
 */
public class Transform3Quat extends Transform3
{
	public final Quaternionf rotation = new Quaternionf();

	protected final Vector3f eulerAngles = new Vector3f();
	protected final Vector3f eulerRadians = new Vector3f();

	protected Transform3Quat()
	{
	}

	@Override
	public Vector3fc eulerRadians()
	{
		return this.rotation.getEulerAnglesXYZ(this.eulerRadians);
	}

	@Override
	public Vector3fc eulerAngles()
	{
		return this.rotation.getEulerAnglesXYZ(this.eulerRadians).mul(RAD2DEG);
	}

	@Override
	public Quaternionfc rotation()
	{
		return this.rotation;
	}

	@Override
	public void rotate(Quaternionfc rot)
	{
		this.rotation.mul(rot);
	}

	@Override
	public void rotate(float radian, Vector3fc axis)
	{
		this.rotation.rotateAxis(radian, axis);
	}

	@Override
	public void rotate(float pitch, float yaw, float roll)
	{
		this.rotation.rotateXYZ(pitch, yaw, roll);
	}

	@Override
	public void setPitch(float pitch)
	{
		this.rotation.rotationX(pitch);
	}

	@Override
	public void setYaw(float yaw)
	{
		this.rotation.rotationY(yaw);
	}

	@Override
	public void setRoll(float roll)
	{
		this.rotation.rotationZ(roll);
	}

	@Override
	public void setEulerRadians(float pitch, float yaw, float roll)
	{
		this.rotation.rotationXYZ(pitch, yaw, roll);
	}

	@Override
	public void setRotation(Quaternionfc rotation)
	{
		this.rotation.set(rotation);
	}
}
