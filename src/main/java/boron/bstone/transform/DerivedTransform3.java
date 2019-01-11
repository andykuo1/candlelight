package boron.bstone.transform;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/19/17.
 */
public class DerivedTransform3 extends Transform3
{
	private final Transform3c parent;

	public DerivedTransform3(Transform3c parent)
	{
		this.parent = parent;
	}

	@Override
	public Vector3f getPosition(Vector3f dst)
	{
		this.parent.getPosition(dst);
		dst.add(super.position);
		return dst;
	}

	@Override
	public Quaternionf getRotation(Quaternionf dst)
	{
		this.parent.getRotation(dst);
		dst.mul(super.rotation);
		return dst;
	}

	@Override
	public Vector3f getScale(Vector3f dst)
	{
		this.parent.getScale(dst);
		dst.mul(super.scale);
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

	public Quaternionfc quaternionOffset()
	{
		return super.rotation;
	}

	public Vector3fc positionOffset()
	{
		return super.position;
	}

	public Vector3fc scaleOffset()
	{
		return super.scale;
	}
}
