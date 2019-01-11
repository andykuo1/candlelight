package apricot.base;

import apricot.base.gridmap.GridMap;

import org.joml.Vector2i;
import apricot.qsilver.poma.Poma;

import java.util.function.Predicate;

/**
 * Created by Andy on 9/13/17.
 */
@Deprecated
public class OldMapUtil
{
	public static boolean contains(GridMap map, int x, int y, int w, int h, Object value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.containsKey(x, y) && map.containsKey(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				if (value.equals(map.getValue(x + i, y + j)))
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
		Poma.ASSERT(map.containsKey(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.containsKey(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				map.setValue(x + i, y + j, value);
			}
		}
	}

	public static <T> void set(GridMap<T> map, int x, int y, GridMap<T> values)
	{
		Poma.ASSERT(map.containsKey(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.containsKey(x + values.getWidth() - 1, y + values.getHeight() - 1));

		for (int i = 0; i < values.getWidth(); ++i)
		{
			for (int j = 0; j < values.getHeight(); ++j)
			{
				map.setValue(x + i, y + j, values.getValue(i, j));
			}
		}
	}

	public static <T> void overlay(GridMap<T> map, int x, int y, GridMap<T> values)
	{
		Poma.ASSERT(map.containsKey(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.containsKey(x + values.getWidth() - 1, y + values.getHeight() - 1));

		for (int i = 0; i < values.getWidth(); ++i)
		{
			for (int j = 0; j < values.getHeight(); ++j)
			{
				T v = values.getValue(i, j);
				if (!v.equals(0))
				{
					map.setValue(x + i, y + j, v);
				}
			}
		}
	}

	public static <T> void replace(GridMap<T> map, int x, int y, int w, int h, T value, Predicate<T> canReplace)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.containsKey(x, y) && map.containsKey(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				T v = map.getValue(x + i, y + j);
				if (canReplace.test(v))
				{
					map.setValue(x + i, y + j, value);
				}
			}
		}
	}

	public static <T> GridMap<T> copy(GridMap<T> map, int x, int y, int w, int h, GridMap<T> dst)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.containsKey(x, y) && map.containsKey(x + w - 1, y + h - 1));

		for (int i = 0; i < dst.getWidth(); ++i)
		{
			for (int j = 0; j < dst.getHeight(); ++j)
			{
				dst.setValue(i, j, map.getValue(x + i, y + j));
			}
		}
		return dst;
	}
}
