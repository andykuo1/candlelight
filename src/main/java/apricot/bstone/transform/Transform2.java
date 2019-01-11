package apricot.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/13/17.
 */
public class Transform2 implements Transform2c
{
	public final Vector2f position = new Vector2f();
	public float radians = 0F;
	public final Vector2f scale = new Vector2f(1, 1);

	public Transform2 setPosition(float x, float y)
	{
		this.position.set(x, y);
		return this;
	}

	public Transform2 setRotation(float radians)
	{
		this.radians = radians;
		return this;
	}

	public Transform2 setScale(float x, float y)
	{
		this.scale.set(x, y);
		return this;
	}

	@Override
	public Vector3f getPosition(Vector3f dst)
	{
		return dst.set(this.position.x(), this.position.y(), 0);
	}

	@Override
	public Quaternionf getRotation(Quaternionf dst)
	{
		return dst.rotationZ(this.radians);
	}

	@Override
	public Vector3f getScale(Vector3f dst)
	{
		return dst.set(this.scale.x(), this.scale.y(), 1);
	}

	@Override
	public Quaternionfc quaternion()
	{
		return new Quaternionf().rotationZ(this.radians);
	}

	@Override
	public Vector3fc position3()
	{
		return new Vector3f(this.position.x(), this.position.y(), 0);
	}

	@Override
	public Vector3fc scale3()
	{
		return new Vector3f(this.scale.x(), this.scale.y(), 1);
	}

	@Override
	public float angles()
	{
		return this.radians * RAD2DEG;
	}

	@Override
	public float radians()
	{
		return this.radians;
	}

	@Override
	public Vector2fc position2()
	{
		return this.position;
	}

	@Override
	public Vector2fc scale2()
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

	public void moveUp(float magnitude)
	{
		this.translate(this.getUp(_VEC2), magnitude);
	}

	public void moveRight(float magnitude)
	{
		this.translate(this.getRight(_VEC2), magnitude);
	}

	public void translate(Vector2fc dir, float magnitude)
	{
		this.position.add(dir.x() * magnitude, dir.y() * magnitude);
	}

	public void translate(float dx, float dy)
	{
		this.position.add(dx, dy);
	}


	//SCALE

	public void scale(float dx, float dy)
	{
		this.scale.mul(dx, dy);
	}


	//ROTATION

	public void rotate(Quaternionfc rot)
	{
		this.radians += rot.getEulerAnglesXYZ(_VEC3).z();
	}

	public void rotate(float radians)
	{
		this.radians += radians;
	}
}
