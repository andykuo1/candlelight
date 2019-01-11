package canary.zilar.dungeon;

import canary.bstone.util.grid.GridMap;
import org.joml.Vector2i;

import java.util.function.Predicate;

/**
 * Created by Andy on 9/13/17.
 */
@Deprecated
public class MapUtil
{
	public static boolean contains(GridMap map, int x, int y, int w, int h, Object value)
	{
		if (w < 0 || h < 0) throw new IllegalArgumentException("must be positive int");
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");
		if (!(map.width() - x >= w && map.height() - y >= h)) throw new IllegalArgumentException("w or h is not within bounds");

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
		if (w < 0 || h < 0) throw new IllegalArgumentException("must be positive int");
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");
		if (!(map.width() - x >= w && map.height() - y >= h)) throw new IllegalArgumentException("w or h is not within bounds");

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
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");

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
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");

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
		if (w < 0 || h < 0) throw new IllegalArgumentException("must be positive int");
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");
		if (!(map.width() - x >= w && map.height() - y >= h)) throw new IllegalArgumentException("w or h is not within bounds");

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
		if (w < 0 || h < 0) throw new IllegalArgumentException("must be positive int");
		if (!(x >= 0 && x < map.width() && y >= 0 && y < map.height())) throw new IllegalArgumentException("x or y is not within bounds");
		if (!(map.width() - x >= w && map.height() - y >= h)) throw new IllegalArgumentException("w or h is not within bounds");

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
