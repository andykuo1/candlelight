package net.jimboi.mod.transform;

import org.joml.Matrix4fc;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/18/17.
 */
public interface Transform extends LocalDirectionVector
{
	float PI = (float) Math.PI;
	float HALF_PI = PI / 2;
	float PI2 = PI * 2;
	float RAD2ANG = 180F / PI;
	float ANG2RAD = PI / 180F;

	Matrix4fc transformation();

	Vector3fc position();
	Vector3fc eulerAngles();
	Vector3fc eulerRadians();
	Vector3fc scale();

	void moveForward(float dist);
	void moveRight(float dist);
	void moveUp(float dist);

	void moveX(float dist);
	void moveY(float dist);
	void moveZ(float dist);

	void setPosition(float x, float y, float z);
}
