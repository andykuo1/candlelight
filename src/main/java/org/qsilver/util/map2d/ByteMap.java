package org.qsilver.util.map2d;

import org.joml.Vector2i;
import org.qsilver.poma.Poma;

import java.util.function.Predicate;

public class ByteMap
{
	public static boolean contains(ByteMap map, int x, int y, int w, int h, byte value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.contains(x, y) && map.contains(x + w - 1, y + h - 1));

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

	public static void fill(ByteMap map, int x, int y, int w, int h, byte value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.contains(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.contains(x + w - 1, y + h - 1));

		for (int i = 0; i < w; ++i)
		{
			for (int j = 0; j < h; ++j)
			{
				map.set(x + i, y + j, value);
			}
		}
	}

	public static void set(ByteMap map, int x, int y, ByteMap values)
	{
		Poma.ASSERT(map.contains(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.contains(x + values.width - 1, y + values.height - 1));

		for (int i = 0; i < values.width; ++i)
		{
			for (int j = 0; j < values.height; ++j)
			{
				map.set(x + i, y + j, values.get(i, j));
			}
		}
	}

	public static void overlay(ByteMap map, int x, int y, ByteMap values)
	{
		Poma.ASSERT(map.contains(x, y), new Vector2i(x, y));
		Poma.ASSERT(map.contains(x + values.width - 1, y + values.height - 1));

		for (int i = 0; i < values.width; ++i)
		{
			for (int j = 0; j < values.height; ++j)
			{
				byte v = values.get(i, j);
				if (v != 0)
				{
					map.set(x + i, y + j, v);
				}
			}
		}
	}

	public static void replace(ByteMap map, int x, int y, int w, int h, byte value, Predicate<Integer> canReplace)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.contains(x, y) && map.contains(x + w - 1, y + h - 1));

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

	public static ByteMap copy(ByteMap map, int x, int y, int w, int h)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.contains(x, y) && map.contains(x + w - 1, y + h - 1));

		ByteMap ret = new ByteMap(w, h);
		for (int i = 0; i < ret.width; ++i)
		{
			for (int j = 0; j < ret.height; ++j)
			{
				ret.set(i, j, map.get(x + i, y + j));
			}
		}
		return ret;
	}

	protected final byte[] map;
	public final int width;
	public final int height;

	public ByteMap(int width, int height)
	{
		Poma.ASSERT(width > 0);
		Poma.ASSERT(height > 0);

		this.map = new byte[width * height];
		this.width = width;
		this.height = height;
	}

	public boolean contains(int x, int y)
	{
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	public void set(int index, byte value)
	{
		this.map[index] = value;
	}

	public byte get(int index)
	{
		return this.map[index];
	}

	public void set(int x, int y, byte value)
	{
		Poma.ASSERT(this.contains(x, y), new Vector2i(x, y));

		this.map[x + y * this.width] = value;
	}

	public byte get(int x, int y)
	{
		Poma.ASSERT(this.contains(x, y), new Vector2i(x, y));

		return this.map[x + y * this.width];
	}

	public int indexOf(int x, int y)
	{
		Poma.ASSERT(this.contains(x, y), new Vector2i(x, y));

		return x + y * this.width;
	}

	public int size()
	{
		return this.width * this.height;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int y = 0; y < this.height; ++y)
		{
			for(int x = 0; x < this.width; ++x)
			{
				sb.append(this.map[x + y * this.width]);
				sb.append(',');
				sb.append(' ');
			}
			sb.append('\n');
		}
		sb.append(']');
		return sb.toString();
	}
}
