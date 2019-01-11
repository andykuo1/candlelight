package apricot.base.gridmap;

/**
 * Created by Andy on 9/13/17.
 */
public class IntMap extends GridMap<Integer>
{
	protected final int[] map;

	public IntMap(int width, int height)
	{
		super(width, height);

		this.map = new int[this.length];
	}

	@Override
	public Integer setValue(int index, Integer value)
	{
		return this.set(index, value);
	}

	@Override
	public Integer getValue(int index)
	{
		return this.get(index);
	}

	public void putAll(IntMap map)
	{
		for(int i = 0; i < map.width && i < this.width; ++i)
		{
			for(int j = 0; j < map.height && j < this.height; ++j)
			{
				this.map[i + j * this.width] = map.map[i + j * map.width];
			}
		}
	}

	public int put(int x, int y, int value)
	{
		if (!this.containsKey(x, y))
		{
			throw new NullPointerException("out of bounds");
		}

		final int i = x + y * this.width;
		final int ret = this.map[i];
		this.map[i] = value;
		return ret;
	}

	public int set(int i, int v)
	{
		int ret = this.map[i];
		this.map[i] = v;
		return ret;
	}

	public int get(int x, int y)
	{
		if (!this.containsKey(x, y))
		{
			throw new NullPointerException("out of bounds");
		}

		return this.map[x + y * this.width];
	}

	public int get(int i)
	{
		return this.map[i];
	}
}
