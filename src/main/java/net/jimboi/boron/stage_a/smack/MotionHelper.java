package net.jimboi.boron.stage_a.smack;

import org.bstone.transform.Transform3c;
import org.joml.Vector2f;

/**
 * Created by Andy on 8/6/17.
 */
public class MotionHelper
{
	public static Vector2f getDirectionTowards(Transform3c from, float x, float y, Vector2f dst)
	{
		return dst.set(x - from.position3().x(), y - from.position3().y()).normalize();
	}

	public static float getRadiansFromDirection(float dx, float dy)
	{
		return (float) Math.atan2(dy, dx);
	}

	public static boolean isWithinDistanceSquared(Transform3c from, float x, float y, float distSqu)
	{
		return from.position3().distanceSquared(x, y, 0) <= distSqu;
	}

	public static boolean isWithinDistanceSquared(Transform3c from, Transform3c to, float distSqu)
	{
		return isWithinDistanceSquared(from, to.position3().x(), to.position3().y(), distSqu);
	}
}
