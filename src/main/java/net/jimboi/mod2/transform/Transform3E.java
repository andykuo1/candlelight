package net.jimboi.mod2.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 4/30/17.
 */
public class Transform3E extends Transform3
{
	public final Vector3f radians = new Vector3f();

	protected final Quaternionf rotation = new Quaternionf();
	protected final Vector3f eulerAngles = new Vector3f();

	protected Transform3E()
	{
	}

	@Override
	public Vector3fc eulerRadians()
	{
		return this.radians;
	}

	@Override
	public Vector3fc eulerAngles()
	{
		return this.eulerAngles.set(this.radians).mul(RAD2ANG);
	}

	@Override
	public Quaternionfc rotation()
	{
		return this.rotation.rotationXYZ(this.radians.x, this.radians.y, this.radians.z);
	}

	@Override
	public void rotate(Quaternionfc rot)
	{
		this.rotation.rotationXYZ(this.radians.x, this.radians.y, this.radians.z);
		this.rotation.mul(rot);
		this.rotation.getEulerAnglesXYZ(this.radians);
	}

	@Override
	public void rotate(float radian, Vector3fc axis)
	{
		this.rotation.rotationXYZ(this.radians.x, this.radians.y, this.radians.z);
		this.rotation.rotateAxis(radian, axis);
		this.rotation.getEulerAnglesXYZ(this.radians);
	}

	@Override
	public void rotate(float pitch, float yaw, float roll)
	{
		this.radians.add(pitch, yaw, roll);
	}

	@Override
	public void setPitch(float pitch)
	{
		this.radians.x = pitch;
	}

	@Override
	public void setYaw(float yaw)
	{
		this.radians.y = yaw;
	}

	@Override
	public void setRoll(float roll)
	{
		this.radians.z = roll;
	}

	@Override
	public void setEulerRadians(float pitch, float yaw, float roll)
	{
		this.radians.set(pitch, yaw, roll);
	}

	@Override
	public void setRotation(Quaternionfc rotation)
	{
		rotation.getEulerAnglesXYZ(this.radians);
	}
}
