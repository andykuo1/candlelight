package org.bstone.util.grid;

import org.joml.Vector2ic;

/**
 * Created by Andy on 11/13/17.
 */
public class ByteMap extends GridMap<Byte>
{
	private final byte[] array;

	public ByteMap(int width, int height)
	{
		super(width, height);

		this.array = new byte[width * height];
	}

	@Override
	public Byte set(Vector2ic key, Byte value)
	{
		return this.set(key.x(), key.y(), value);
	}

	@Override
	public Byte get(Vector2ic key)
	{
		return this.get(key.x(), key.y());
	}

	public byte set(int x, int y, byte value)
	{
		int i = x + y * this.width;
		byte flag = this.array[i];
		this.array[i] = value;
		return flag;
	}

	public void add(int x, int y, byte offset)
	{
		this.array[x + y * this.width] += offset;
	}

	public byte get(int x, int y)
	{
		return this.array[x + y * this.width];
	}

	public byte[] array()
	{
		return this.array;
	}
}
