package net.jimboi.stage_b.gnome.transform;

import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/18/17.
 */
public interface Transform extends DirectionVectors
{
	float PI = (float) Math.PI;
	float PI2 = PI * 2;
	float HALF_PI = PI / 2;
	float RAD2ANG = 180F / PI;
	float ANG2RAD = PI / 180F;

	Matrix4fc transformation();

	Vector3fc position();
	Vector3fc eulerAngles();
	Vector3fc eulerRadians();

	Quaternionfc rotation();
	Vector3fc scale();
}
