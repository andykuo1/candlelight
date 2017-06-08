package net.jimboi.mod2.transform;

import org.joml.Vector3f;

/**
 * Created by Andy on 5/22/17.
 */
public interface DirectionVectors
{
	Vector3f getForward(Vector3f dst);

	Vector3f getUp(Vector3f dst);

	Vector3f getRight(Vector3f dst);
}
