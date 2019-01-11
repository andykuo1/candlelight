package apricot.bstone.transform;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/13/17.
 */
public interface Transform2c extends Transform3c, DirectionVector2
{
	Vector2fc XAXIS = new Vector2f(1, 0);
	Vector2fc YAXIS = new Vector2f(0, 1);

	Vector2fc IDENTITY = new Vector2f(1, 1);
	Vector2fc ZERO = new Vector2f(0, 0);

	@Override
	default Vector2f getUp(Vector2f dst)
	{
		float rad = this.radians();
		return dst.set((float) Math.cos(rad), (float) Math.sin(rad));
	}

	@Override
	default Vector2f getRight(Vector2f dst)
	{
		float rad = this.radians() + HALF_PI;
		return dst.set((float) Math.cos(rad), (float) Math.sin(rad));
	}

	float angles();
	float radians();

	Vector2fc position2();
	Vector2fc scale2();

	@Override
	default float posX()
	{
		return this.position2().x();
	}

	@Override
	default float posY()
	{
		return this.position2().y();
	}

	@Override
	default float posZ()
	{
		return 0;
	}

	default DerivedTransform2 derive2()
	{
		return new DerivedTransform2(this);
	}
}
