package net.jimboi.physx;

/**
 * Created by Andy on 5/17/17.
 */
public class Circle
{
	public int radius;
	public int x;
	public int y;

	public static boolean intersection(Circle a, Circle b)
	{
		int r = a.radius + b.radius;
		r *= r;
		int x = a.x + b.x;
		int y = a.y + b.y;
		return r < x * x + y * y;
	}
}
