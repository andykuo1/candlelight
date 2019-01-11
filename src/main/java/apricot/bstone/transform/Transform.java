package apricot.bstone.transform;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Andy on 7/13/17.
 */
public interface Transform extends DirectionVector3
{
	float PI = (float) Math.PI;
	float PI2 = PI * 2;
	float HALF_PI = PI / 2;
	float RAD2DEG = 180F / PI;
	float DEG2RAD = PI / 180F;

	Vector2f _VEC2 = new Vector2f();
	Vector3f _VEC3 = new Vector3f();
	Quaternionf _QUAT = new Quaternionf();

	default Matrix4f getTransformation(Matrix4f dst)
	{
		return dst.identity().translate(this.getPosition(_VEC3)).rotate(this.getRotation(_QUAT)).scale(this.getScale(_VEC3));
	}

	Vector3f getPosition(Vector3f dst);
	Quaternionf getRotation(Quaternionf dst);
	Vector3f getScale(Vector3f dst);
}
