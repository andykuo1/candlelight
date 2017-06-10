package org.qsilver.util.map2d;

import org.joml.Vector2i;
import org.qsilver.poma.Poma;

import java.util.function.Predicate;

public class IntMap
{
	public static boolean contains(IntMap map, int x, int y, int w, int h, int value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.isValid(x, y) && map.isValid(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				if (map.get(x + i, y + j) == value)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static void fill(IntMap map, int x, int y, int w, int h, int value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.isValid(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.isValid(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				map.set(x + i, y + j, value);
			}
		}
	}

	public static void set(IntMap map, int x, int y, IntMap values)
	{
		Poma.ASSERT(map.isValid(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.isValid(x + values.width - 1, y + values.height - 1));

		for (int i = 0; i < values.width; ++i)
		{
			for (int j = 0; j < values.height; ++j)
			{
				map.set(x + i, y + j, values.get(i, j));
			}
		}
	}

	public static void overlay(IntMap map, int x, int y, IntMap values)
	{
		Poma.ASSERT(map.isValid(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.isValid(x + values.width - 1, y + values.height - 1));

		for (int i = 0; i < values.width; ++i)
		{
			for (int j = 0; j < values.height; ++j)
			{
				int v = values.get(i, j);
				if (v != 0)
				{
					map.set(x + i, y + j, v);
				}
			}
		}
	}

	public static void replace(IntMap map, int x, int y, int w, int h, int value, Predicate<Integer> canReplace)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.isValid(x, y) && map.isValid(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				int v = map.get(x + i, y + j);
				if (canReplace.test(v))
				{
					map.set(x + i, y + j, value);
				}
			}
		}
	}

	public static IntMap copy(IntMap map, int x, int y, int w, int h)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.isValid(x, y) && map.isValid(x + w - 1, y + h - 1));

		IntMap ret = new IntMap(w, h);
		for (int i = 0; i < ret.width; ++i)
		{
			for (int j = 0; j < ret.height; ++j)
			{
				ret.set(i, j, map.get(x + i, y + j));
			}
		}
		return ret;
	}

	protected final int[] map;
	public final int width;
	public final int height;

	public IntMap(int width, int height)
	{
		Poma.ASSERT(width > 0);
		Poma.ASSERT(height > 0);

		this.map = new int[width * height];
		this.width = width;
		this.height = height;
	}

	public boolean isValid(int x, int y)
	{
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	public void set(int x, int y, int value)
	{
		Poma.ASSERT(this.isValid(x, y), new Vector2i(x, y));

		this.map[x + y * this.width] = value;
	}

	public int get(int x, int y)
	{
		Poma.ASSERT(this.isValid(x, y), new Vector2i(x, y));

		return this.map[x + y * this.width];
	}

	public int indexOf(int x, int y)
	{
		Poma.ASSERT(this.isValid(x, y), new Vector2i(x, y));

		return x + y * this.width;
	}

	public int size()
	{
		return this.width * this.height;
	}
}
