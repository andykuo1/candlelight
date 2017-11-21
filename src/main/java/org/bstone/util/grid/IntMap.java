package org.bstone.util.grid;

import org.joml.Vector2ic;

/**
 * Created by Andy on 11/13/17.
 */
public class IntMap extends GridMap<Integer>
{
	private final int[] array;

	public IntMap(int width, int height)
	{
		super(width, height);

		this.array = new int[width * height];
	}

	@Override
	public Integer set(Vector2ic key, Integer value)
	{
		return this.set(key.x(), key.y(), value);
	}

	@Override
	public Integer get(Vector2ic key)
	{
		return this.get(key.x(), key.y());
	}

	public int set(int x, int y, int value)
	{
		int i = x + y * this.width;
		int flag = this.array[i];
		this.array[i] = value;
		return flag;
	}

	public void add(int x, int y, int offset)
	{
		this.array[x + y * this.width] += offset;
	}

	public int get(int x, int y)
	{
		return this.array[x + y * this.width];
	}

	public int[] array()
	{
		return this.array;
	}
}
