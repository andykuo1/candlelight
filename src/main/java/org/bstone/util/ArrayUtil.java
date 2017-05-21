package org.bstone.util;

import java.util.Iterator;

/**
 * Created by Andy on 5/10/17.
 */
public class ArrayUtil
{
	public static int[] toIntArray(Iterator<Integer> iter, int[] dst, int offset)
	{
		int i = offset;
		while(iter.hasNext() && i < dst.length)
		{
			dst[i++] = iter.next();
		}
		return dst;
	}
}
