package org.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/13/17.
 */
public class Transform3 extends Transform3c
{
	public final Vector3f position = new Vector3f();
	public final Quaternionf rotation = new Quaternionf();
	public final Vector3f scale = new Vector3f(1, 1, 1);

	@Override
	public Vector3f getPosition(Vector3f dst)
	{
		return dst.set(this.position);
	}

	@Override
	public Quaternionf getRotation(Quaternionf dst)
	{
		return dst.set(this.rotation);
	}

	@Override
	public Vector3f getScale(Vector3f dst)
	{
		return dst.set(this.scale);
	}

	@Override
	public Quaternionfc quaternion()
	{
		return this.rotation;
	}

	@Override
	public Vector3fc position3()
	{
		return this.position;
	}

	@Override
	public Vector3fc scale3()
	{
		return this.scale;
	}

	//POSITION

	public void moveX(float dist)
	{
		this.position.x += dist;
	}

	public void moveY(float dist)
	{
		this.position.y += dist;
	}

	public void moveZ(float dist)
	{
		this.position.z += dist;
	}

	public void moveForward(float magnitude)
	{
		this.translate(this.getForward(this.vec3), magnitude);
	}

	public void moveUp(float magnitude)
	{
		this.translate(this.getUp(this.vec3), magnitude);
	}

	public void moveRight(float magnitude)
	{
		this.translate(this.getRight(this.vec3), magnitude);
	}

	public void translate(Vector3fc dir, float magnitude)
	{
		this.position.add(dir.x() * magnitude, dir.y() * magnitude, dir.z() * magnitude);
	}

	public void translate(float dx, float dy, float dz)
	{
		this.position.add(dx, dy, dz);
	}


	//SCALE

	public void scale(float dx, float dy, float dz)
	{
		this.scale.mul(dx, dy, dz);
	}


	//ROTATION

	public void setEulerRadians(float pitch, float yaw, float roll)
	{
		this.rotation.rotationXYZ(pitch, yaw, roll);
	}

	public void setPitch(float pitch)
	{
		this.rotation.rotationX(pitch);
	}

	public void setYaw(float yaw)
	{
		this.rotation.rotationY(yaw);
	}

	public void setRoll(float roll)
	{
		this.rotation.rotationZ(roll);
	}

	public void rotate(Quaternionfc rot)
	{
		this.rotation.mul(rot);
	}

	public void rotate(float radian, Vector3fc axis)
	{
		this.rotation.rotateAxis(radian, axis);
	}

	public void rotate(float pitch, float yaw, float roll)
	{
		this.rotation.rotateXYZ(pitch, yaw, roll);
	}
}
