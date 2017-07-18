package net.jimboi.apricot.stage_a.blob.torchlite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class ArrayUtil
{
	private ArrayUtil() {}

	public static <E> boolean isSquare(E[][] array)
	{
		int i = array.length;
		for(E[] j : array)
		{
			if (j.length != i)
			{
				return false;
			}
		}

		return true;
	}

	public static boolean isSquare(int[][] array)
	{
		int i = array.length;
		for(int[] j : array)
		{
			if (j.length != i)
			{
				return false;
			}
		}

		return true;
	}

	public static boolean isSquare(float[][] array)
	{
		int i = array.length;
		for(float[] j : array)
		{
			if (j.length != i)
			{
				return false;
			}
		}

		return true;
	}

	public static <E> int indexOf(E[] parArray, E parValue)
	{
		assert(parArray != null);

		for(int i = 0; i < parArray.length; ++i)
		{
			if (parValue.equals(parArray[i]))
			{
				return i;
			}
		}

		return -1;
	}

	public static int indexOf(float[] array, float value)
	{
		assert(array != null);

		for(int i = 0; i < array.length; ++i)
		{
			if (value == array[i])
			{
				return i;
			}
		}

		return -1;
	}

	public static <E> void swap(E[] parArray, int parFrom, int parTo)
	{
		assert(parFrom >= 0 && parFrom < parArray.length);
		assert(parTo >= 0 && parTo < parArray.length);

		E a = parArray[parFrom];
		parArray[parFrom] = parArray[parTo];
		parArray[parTo] = a;
	}

	public static <E> E[] shuffle(Random parRandom, E[] parArray)
	{
		for(int i = 1; i < parArray.length; ++i)
		{
			int j = parRandom.nextInt(i);
			E a = parArray[j];
			parArray[j] = parArray[i];
			parArray[i] = a;
		}

		return parArray;
	}

	public static <E> E[] join(E[] array1, E[] array2)
	{
		E[] result = Arrays.copyOf(array1, array1.length + array2.length);
		for(int i = 0; i < array2.length; ++i)
		{
			result[i + array1.length] = array2[i];
		}

		return result;
	}

	public static Integer[] toInteger(int[] parArray)
	{
		Integer[] result = new Integer[parArray.length];

		for(int i = 0; i < parArray.length; ++i)
		{
			result[i] = parArray[i];
		}

		return result;
	}

	public static int[] toInteger(Integer[] array)
	{
		int[] result = new int[array.length];

		for(int i = 0; i < array.length; ++i)
		{
			result[i] = array[i];
		}

		return result;
	}

	public static Float[] toFloat(float[] parArray)
	{
		Float[] result = new Float[parArray.length];

		for(int i = 0; i < parArray.length; ++i)
		{
			result[i] = parArray[i];
		}

		return result;
	}

	public static float[] toFloat(Float[] array)
	{
		float[] result = new float[array.length];

		for(int i = 0; i < array.length; ++i)
		{
			result[i] = array[i];
		}

		return result;
	}
	public static List<Integer> toList(int[] array)
	{
		List<Integer> list = new ArrayList<Integer>();
		for(Integer i : array)
		{
			list.add(i);
		}

		return list;
	}

	public static <E> List<E> toList(E[] array)
	{
		List<E> list = new ArrayList<E>();
		for(E e : array)
		{
			list.add(e);
		}

		return list;
	}

	public static <E> Set<E> toSet(E[] array)
	{
		Set<E> set = new HashSet<E>();
		for(E e : array)
		{
			set.add(e);
		}

		return set;
	}

	public static <E> String toString(E[] array)
	{
		StringBuilder str = new StringBuilder();

		str.append("[");
		boolean flag = false;
		for(E e : array)
		{
			if (!flag)
			{
				flag = true;
			}
			else
			{
				str.append(", ");
			}
			str.append(e);
		}
		str.append("]");

		return str.toString();
	}
}
