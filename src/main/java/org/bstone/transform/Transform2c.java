package org.bstone.transform;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class Transform2c extends Transform3c implements DirectionVector2
{
	public static final Vector2fc XAXIS = new Vector2f(1, 0);
	public static final Vector2fc YAXIS = new Vector2f(0, 1);

	public static final Vector2fc IDENTITY = new Vector2f(1, 1);
	public static final Vector2fc ZERO = new Vector2f(0, 0);

	protected final Vector2f vec2 = new Vector2f();

	@Override
	public final Vector2f getUp(Vector2f dst)
	{
		float rad = this.radians();
		return dst.set((float) Math.cos(rad), (float) Math.sin(rad));
	}

	@Override
	public final Vector2f getRight(Vector2f dst)
	{
		float rad = this.radians() + HALF_PI;
		return dst.set((float) Math.cos(rad), (float) Math.sin(rad));
	}

	public abstract float angles();
	public abstract float radians();

	public abstract Vector2fc position2();
	public abstract Vector2fc scale2();

	public final DerivedTransform2 derive2()
	{
		return new DerivedTransform2(this);
	}
}
