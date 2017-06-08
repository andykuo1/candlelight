package net.jimboi.mod.torchlite;

public class IntMap
{
	private final int[] map;
	public final int width;
	public final int height;

	public IntMap(int width, int height)
	{
		Log.ASSERT(width > 0);
		Log.ASSERT(height > 0);

		this.map = new int[width * height];
		this.width = width;
		this.height = height;
	}

	public boolean contains(int beginX, int beginY, int endX, int endY, int value)
	{
		Log.ASSERT(beginX < endX);
		Log.ASSERT(beginY < endY);
		Log.ASSERT(this.isValid(beginX, beginY) && this.isValid(endX, endY));

		for(int i = beginY; i < endY; ++i)
		{
			for(int j = beginX; j < endX; ++j)
			{
				if (this.map[i + j * this.width] == value)
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValue(int x, int y, int value)
	{
		Log.ASSERT(this.isValid(x, y), new Vec2i(x, y));

		return this.map[x + y * this.width] == value;
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

	public void fill(int beginX, int beginY, int endX, int endY, int value)
	{
		Log.ASSERT(beginX < endX);
		Log.ASSERT(beginY < endY);
		Log.ASSERT(this.isValid(beginX, beginY) && this.isValid(endX, endY));

		for(int i = beginY; i < endY; ++i)
		{
			for(int j = beginX; j < endX; ++j)
			{
				this.map[j + i * this.width] = value;
			}
		}
	}

	public void set(int x, int y, int value)
	{
		Log.ASSERT(this.isValid(x, y), new Vec2i(x, y));

		this.map[x + y * this.width] = value;
	}

	public int get(int x, int y)
	{
		Log.ASSERT(this.isValid(x, y), new Vec2i(x, y));

		return this.map[x + y * this.width];
	}

	public void set(int index, int value)
	{
		this.map[index] = value;
	}

	public int get(int index)
	{
		return this.map[index];
	}

	public int indexOf(int x, int y)
	{
		Log.ASSERT(this.isValid(x, y), new Vec2i(x, y));

		return x + y * this.width;
	}

	public int size()
	{
		return this.map.length;
	}

	public int[] toIntArray()
	{
		return this.map;
	}
}
