package net.jimboi.mod.transform;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class Transform3 implements Transform
{
	public static boolean USE_EULER = false;

	public static Transform3 create()
	{
		return USE_EULER ? new Transform3E() : new Transform3Q();
	}

	public static final Vector3fc XAXIS = new Vector3f(1, 0, 0);
	public static final Vector3fc YAXIS = new Vector3f(0, 1, 0);
	public static final Vector3fc ZAXIS = new Vector3f(0, 0, 1);
	public static final Vector3fc IDENTITY = new Vector3f(1, 1, 1);
	public static final Vector3fc ZERO = new Vector3f(0, 0, 0);

	public final Vector3f position = new Vector3f();
	public final Vector3f scale = new Vector3f(1, 1, 1);

	protected final Matrix4f transformation = new Matrix4f();

	protected final Vector3f _target = new Vector3f();

	@Override
	public Matrix4fc transformation()
	{
		this.transformation.identity();
		this.transformation.translate(this.position);
		this.transformation.rotate(this.rotation());
		this.transformation.scale(this.scale);
		return this.transformation;
	}

	@Override
	public Vector3fc position()
	{
		return this.position;
	}

	@Override
	public Vector3fc scale()
	{
		return this.scale;
	}

	@Override
	public Vector3f getForward(Vector3f dst)
	{
		return this.rotation().normalizedPositiveZ(dst);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.rotation().normalizedPositiveX(dst);
	}

	@Override
	public Vector3f getUp(Vector3f dst)
	{
		return this.rotation().normalizedPositiveY(dst);
	}

	@Override
	public void moveX(float dist)
	{
		this.position.x += dist;
	}

	@Override
	public void moveY(float dist)
	{
		this.position.y += dist;
	}

	@Override
	public void moveZ(float dist)
	{
		this.position.z += dist;
	}

	@Override
	public void moveForward(float magnitude)
	{
		this.translate(this.getForward(this._target), magnitude);
	}

	@Override
	public void moveUp(float magnitude)
	{
		this.translate(this.getUp(this._target), magnitude);
	}

	@Override
	public void moveRight(float magnitude)
	{
		this.translate(this.getRight(this._target), magnitude);
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		this.position.set(x, y, z);
	}

	public void translate(Vector3fc dir, float magnitude)
	{
		this.position.add(dir.x() * magnitude, dir.y() * magnitude, dir.z() * magnitude);
	}

	public void translate(float dx, float dy, float dz)
	{
		this.position.add(dx, dy, dz);
	}

	public void scale(float dx, float dy, float dz)
	{
		this.scale.mul(dx, dy, dz);
	}

	public abstract Quaternionfc rotation();

	public abstract void rotate(Quaternionfc rot);

	public abstract void rotate(float radian, Vector3fc axis);

	public abstract void rotate(float pitch, float yaw, float roll);

	public abstract void setPitch(float pitch);

	public abstract void setYaw(float yaw);

	public abstract void setRoll(float roll);

	public abstract void setEulerRadians(float pitch, float yaw, float roll);

	public abstract void setRotation(Quaternionfc rotation);
}
