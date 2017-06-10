package org.qsilver.util;

/**
 * Created by Andy on 5/20/17.
 */
public class MathUtil
{
	public static int clamp(int value, int min, int max)
	{
		return value < min ? min : value > max ? max : value;
	}

	public static float clamp(float value, float min, float max)
	{
		return value < min ? min : value > max ? max : value;
	}

	public static int sign(int value)
	{
		return value < 0 ? -1 : 1;
	}

	public static float sign(float value)
	{
		return value < 0 ? -1 : 1;
	}
}
