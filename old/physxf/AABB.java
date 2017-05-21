package net.jimboi.physx;

/**
 * Created by Andy on 5/17/17.
 */
public class AABB
{
	public int minX;
	public int minY;
	public int maxX;
	public int maxY;

	public static boolean intersection(AABB a, AABB b)
	{
		if (a.maxX < b.minX || a.minX > b.maxX) return false;
		if (a.maxY < b.minY || a.minY > b.maxY) return false;

		return true;
	}
}