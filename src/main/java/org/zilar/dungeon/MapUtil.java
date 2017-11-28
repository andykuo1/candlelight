package org.zilar.dungeon;

import org.bstone.util.grid.GridMap;
import org.joml.Vector2i;
import org.qsilver.poma.Poma;

import java.util.function.Predicate;

/**
 * Created by Andy on 9/13/17.
 */
@Deprecated
public class MapUtil
{
	public static boolean contains(GridMap map, int x, int y, int w, int h, Object value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());
		Poma.ASSERT(map.width() - x >= w && map.height() - y >= h);

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				if (value.equals(map.get(new Vector2i(x + i, y + j))))
				{
					return true;
				}
			}
		}

		return false;
	}

	public static <T> void fill(GridMap<T> map, int x, int y, int w, int h, T value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());
		Poma.ASSERT(map.width() - x >= w && map.height() - y >= h);

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				map.set(new Vector2i(x + i, y + j), value);
			}
		}
	}

	public static <T> void set(GridMap<T> map, int x, int y, GridMap<T> values)
	{
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());

		for (int i = 0; i < values.width(); ++i)
		{
			for (int j = 0; j < values.height(); ++j)
			{
				map.set(new Vector2i(x + i, y + j), values.get(new Vector2i(i, j)));
			}
		}
	}

	public static <T> void overlay(GridMap<T> map, int x, int y, GridMap<T> values)
	{
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());

		for (int i = 0; i < values.width(); ++i)
		{
			for (int j = 0; j < values.height(); ++j)
			{
				T v = values.get(new Vector2i(i, j));
				if (!v.equals(0))
				{
					map.set(new Vector2i(x + i, y + j), v);
				}
			}
		}
	}

	public static <T> void replace(GridMap<T> map, int x, int y, int w, int h, T value, Predicate<T> canReplace)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());
		Poma.ASSERT(map.width() - x >= w && map.height() - y >= h);

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				T v = map.get(new Vector2i(x + i, y + j));
				if (canReplace.test(v))
				{
					map.set(new Vector2i(x + i, y + j), value);
				}
			}
		}
	}

	public static <T> GridMap<T> copy(GridMap<T> map, int x, int y, int w, int h, GridMap<T> dst)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(x >= 0 && x < map.width() && y >= 0 && y < map.height());
		Poma.ASSERT(map.width() - x >= w && map.height() - y >= h);

		for (int i = 0; i < dst.width(); ++i)
		{
			for (int j = 0; j < dst.height(); ++j)
			{
				dst.set(new Vector2i(i, j), map.get(new Vector2i(x + i, y + j)));
			}
		}
		return dst;
	}
}
