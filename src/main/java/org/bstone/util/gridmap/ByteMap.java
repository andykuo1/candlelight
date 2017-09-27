package org.bstone.util.gridmap;

/**
 * Created by Andy on 9/13/17.
 */
public class ByteMap extends GridMap<Byte>
{
	protected final byte[] map;

	public ByteMap(int width, int height)
	{
		super(width, height);

		this.map = new byte[this.length];
	}

	@Override
	public Byte setValue(int index, Byte value)
	{
		return this.set(index, value);
	}

	@Override
	public Byte getValue(int index)
	{
		return this.get(index);
	}

	public void putAll(ByteMap map)
	{
		for(int i = 0; i < map.width && i < this.width; ++i)
		{
			for(int j = 0; j < map.height && j < this.height; ++j)
			{
				this.map[i + j * this.width] = map.map[i + j * map.width];
			}
		}
	}

	public byte put(int x, int y, byte value)
	{
		if (!this.containsKey(x, y))
		{
			throw new NullPointerException("out of bounds");
		}

		final int i = x + y * this.width;
		final byte ret = this.map[i];
		this.map[i] = value;
		return ret;
	}

	public byte set(int i, byte v)
	{
		byte ret = this.map[i];
		this.map[i] = v;
		return ret;
	}

	public byte get(int x, int y)
	{
		if (!this.containsKey(x, y))
		{
			throw new NullPointerException("out of bounds");
		}

		return this.map[x + y * this.width];
	}

	public byte get(int i)
	{
		return this.map[i];
	}
}
