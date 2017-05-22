package org.bstone.util.map2d;

import org.bstone.poma.Poma;
import org.joml.Vector2i;

public class IntMap
{
	public static boolean contains(IntMap map, int x, int y, int w, int h, int value)
	{
		Poma.ASSERT(w >= 0);
		Poma.ASSERT(h >= 0);
		Poma.ASSERT(map.isValid(x, y) && map.isValid(x + w, y + h));

		for(int xw = x + w; x < xw; ++x)
		{
			for(int yh = y + h; y < yh; ++y)
			{
				if (map.get(x, y) == value)
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
		Poma.ASSERT(map.isValid(x, y) && map.isValid(x + w, y + h));

		for(int xw = x + w; x < xw; ++x)
		{
			for(int yh = y + h; y < yh; ++y)
			{
				map.set(x, y, value);
			}
		}
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

	public void fill(int value)
	{
		for(int i = 0; i < this.map.length; ++i)
		{
			this.map[i] = value;
		}
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
