package org.qsilver.util;

import java.util.Iterator;

/**
 * Created by Andy on 5/10/17.
 */
public class ArrayUtil
{
	@SuppressWarnings("unchecked")
	public static <T> T get(Object[] arr, int i)
	{
		if (i >= arr.length || i < 0) return null;
		return (T) arr[i];
	}

	public static <T> T getOrDefault(Object[] arr, int i, T def)
	{
		T t = get(arr, i);
		if (t == null) return def;
		return t;
	}

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
