package canary.bstone.transform;

import org.joml.Vector2f;

/**
 * Created by Andy on 5/22/17.
 */
public interface DirectionVector2
{
	Vector2f getUp(Vector2f dst);

	Vector2f getRight(Vector2f dst);
}
