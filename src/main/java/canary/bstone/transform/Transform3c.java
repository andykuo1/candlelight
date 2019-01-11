package canary.bstone.transform;

import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 7/13/17.
 */
public interface Transform3c extends Transform
{
	Vector3fc XAXIS = new Vector3f(1, 0, 0);
	Vector3fc YAXIS = new Vector3f(0, 1, 0);
	Vector3fc ZAXIS = new Vector3f(0, 0, 1);

	Vector3fc IDENTITY = new Vector3f(1, 1, 1);
	Vector3fc ZERO = new Vector3f(0, 0, 0);

	@Override
	default Vector3f getForward(Vector3f dst)
	{
		return this.getRotation(_QUAT).normalizedPositiveZ(dst);
	}

	@Override
	default Vector3f getRight(Vector3f dst)
	{
		return this.getRotation(_QUAT).normalizedPositiveX(dst);
	}

	@Override
	default Vector3f getUp(Vector3f dst)
	{
		return this.getRotation(_QUAT).normalizedPositiveY(dst);
	}

	default Vector3fc eulerRadians()
	{
		return this.getRotation(_QUAT).getEulerAnglesXYZ(new Vector3f());
	}

	default Vector3fc eulerAngles()
	{
		return this.getRotation(_QUAT).getEulerAnglesXYZ(new Vector3f()).mul(RAD2DEG);
	}

	Quaternionfc quaternion();

	Vector3fc position3();
	Vector3fc scale3();

	default float posX()
	{
		return this.position3().x();
	}

	default float posY()
	{
		return this.position3().y();
	}

	default float posZ()
	{
		return this.position3().z();
	}

	default DerivedTransform3 derive3()
	{
		return new DerivedTransform3(this);
	}
}
