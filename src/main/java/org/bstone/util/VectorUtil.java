package org.bstone.util;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3fc;

import java.util.Iterator;

/**
 * Created by Andy on 5/10/17.
 */
public class VectorUtil
{
	public static float distanceSquared(Vector2fc a, Vector2fc b)
	{
		float x = a.x() - b.x();
		float y = a.y() - b.y();
		return x * x + y * y;
	}

	public static float cross(Vector2fc a, Vector2fc b)
	{
		return a.x() * b.y() - a.y() * b.x();
	}

	public static Vector2f cross(Vector2fc a, float b, Vector2f dst)
	{
		return dst.set(a.y() * b, a.x() * -b);
	}

	public static Vector2f cross(float a, Vector2fc b, Vector2f dst)
	{
		return dst.set(b.y() * -a, b.x() * a);
	}

	public static float[] Vector2ToFloatArray(Iterator<Vector2fc> iter, float[] dst, int offset)
	{
		int i = offset;
		int j = i * 2;
		while(iter.hasNext() && j < dst.length)
		{
			Vector2fc v = iter.next();
			dst[j] = v.x();
			dst[j + 1] = v.y();
			i++;
			j = i * 2;
		}
		return dst;
	}

	public static float[] Vector3ToFloatArray(Iterator<Vector3fc> iter, float[] dst, int offset)
	{
		int i = offset;
		int j = i * 3;
		while(iter.hasNext() && j < dst.length)
		{
			Vector3fc v = iter.next();
			dst[j] = v.x();
			dst[j + 1] = v.y();
			dst[j + 2] = v.z();
			i++;
			j = i * 3;
		}
		return dst;
	}
}
