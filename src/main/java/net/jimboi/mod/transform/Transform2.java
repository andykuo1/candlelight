package net.jimboi.mod.transform;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/18/17.
 */
public class Transform2 implements Transform
{
	public static Transform2 create()
	{
		return new Transform2();
	}

	public static final Vector2fc XAXIS = new Vector2f(1, 0);
	public static final Vector2fc YAXIS = new Vector2f(0, 1);
	public static final Vector2fc IDENTITY = new Vector2f(1, 1);
	public static final Vector2fc ZERO = new Vector2f(0, 0);

	public final Vector3f position = new Vector3f();
	public final Vector3f scale = new Vector3f(1, 1, 1);

	public float radians = 0;
	protected final Quaternionf rotation = new Quaternionf();
	protected final Vector3f eulerAngles = new Vector3f();
	protected final Vector3f eulerRadians = new Vector3f();

	protected final Matrix4f transformation = new Matrix4f();

	protected Transform2()
	{
	}

	@Override
	public Matrix4fc transformation()
	{
		this.transformation.identity();
		this.transformation.translate(this.position.x, this.position.y, 0);
		this.transformation.rotate(this.radians, 0, 0, 1);
		this.transformation.scale(this.scale.x, this.scale.y, 1);
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
		return dst.set(Transform3.ZAXIS);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return dst.set((float) Math.cos(this.radians), (float) Math.sin(this.radians), 0);
	}

	@Override
	public Vector3f getUp(Vector3f dst)
	{
		return dst.set((float) Math.cos(this.radians + HALF_PI), (float) Math.sin(this.radians + HALF_PI), 0);
	}

	public Vector2f getRight(Vector2f dst)
	{
		return dst.set((float) Math.cos(this.radians), (float) Math.sin(this.radians));
	}

	public Vector2f getUp(Vector2f dst)
	{
		return dst.set((float) Math.cos(this.radians + HALF_PI), (float) Math.sin(this.radians + HALF_PI));
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
	public void moveForward(float dist)
	{
		this.position.z += dist;
	}

	@Override
	public void moveRight(float dist)
	{
		this.position.x += dist * (float) Math.cos(this.radians);
		this.position.y += dist * (float) Math.sin(this.radians);
	}

	@Override
	public void moveUp(float dist)
	{
		this.position.x += dist * (float) Math.cos(this.radians + HALF_PI);
		this.position.y += dist * (float) Math.sin(this.radians + HALF_PI);
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		this.position.set(x, y, z);
	}

	public void translate(Vector2fc dir, float magnitude)
	{
		this.position.add(dir.x() * magnitude, dir.y() * magnitude,0);
	}

	public void translate(float dx, float dy)
	{
		this.position.add(dx, dy, 0);
	}

	public void scale(float dx, float dy)
	{
		this.scale.mul(dx, dy, 1);
	}

	@Override
	public Vector3fc eulerRadians()
	{
		return this.eulerRadians.set(0, 0, this.radians);
	}

	@Override
	public Vector3fc eulerAngles()
	{
		return this.eulerAngles.set(0, 0, this.radians * RAD2ANG);
	}

	public float angles()
	{
		return RAD2ANG * this.radians;
	}

	public float radians()
	{
		return this.radians;
	}

	public Quaternionfc rotation()
	{
		return this.rotation.rotationXYZ(0, 0, this.radians);
	}

	public void rotate(float rad)
	{
		this.radians += rad;
	}
}
