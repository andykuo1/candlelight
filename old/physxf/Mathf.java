package net.jimboi.physx;

/**
 * Created by Andy on 5/17/17.
 */
public class Mathf
{
	public static final int F_ONE = Mathf.toFixedFloat(1);

	protected static final int SHIFT_AMT = 12;
	protected static final float SHIFT_POW = (float) Math.pow(2, SHIFT_AMT);
	protected static final int INT_AMT = Integer.BYTES - SHIFT_AMT;

	public static int abs(int a)
	{
		return a < 0 ? -a : a;
	}

	public static int mul(int a, int b)
	{
		int ret = (a * b) >> SHIFT_AMT;
		return ret == 0 ? 1 : ret;
	}

	public static int div(int a, int b)
	{
		int ret = a / (b >> SHIFT_AMT);
		return ret == 0 ? 1 : ret;
	}

	public static int sqrt(int a)
	{
		//TODO: Must be implemented!
		int root = 0;
		int remHi = 0;
		int remLo = a;
		int count = SHIFT_AMT;

		int testDiv;
		do
		{
			remHi = (remHi << INT_AMT) | (remLo >> SHIFT_AMT);
			remLo <<= INT_AMT;
			root <<= 1;
			testDiv = (root << 1) + 1;
			if (remHi >= testDiv)
			{
				remHi -= testDiv;
				root++;
			}
		}
		while (count-- != 0);

		return root;
	}

	public static int inv(int a)
	{
		if (a == 0) return 0;
		int ret = (F_ONE << SHIFT_AMT) / a;
		return ret == 0 ? 1 : ret;
	}

	public static int len(int dx, int dy)
	{
		return sqrt(lenSqu(dx, dy));
	}

	public static int lenSqu(int dx, int dy)
	{
		return dx * dx + dy * dy;
	}

	public static int dotProduct(int x1, int y1, int x2, int y2)
	{
		return mul(x1, x2) + mul(y1, y2);
	}

	public static int min(int a, int b)
	{
		return a <= b ? a : b;
	}

	public static int max(int a, int b)
	{
		return a >= b ? a : b;
	}

	public static int clamp(int min, int max, int val)
	{
		if (val <= min) return min;
		if (val >= max) return max;
		return val;
	}

	public static int toFixedFloat(int i)
	{
		return i << SHIFT_AMT;
	}

	public static float toFloat(int ff)
	{
		return ff / SHIFT_POW;
	}

	public static int toInteger(int ff)
	{
		return ff >> SHIFT_AMT;
	}
}
