package apricot.stage_a.blob.torchlite;

import java.util.HashMap;
import java.util.Map;

public class IntMultiMap extends IntMap
{
	private final Map<Vec2i, IntMap> maps = new HashMap<Vec2i, IntMap>();
	private final int size;

	public IntMultiMap(int width, int height, int size)
	{
		super(width, height);

		this.size = size;

		Log.ASSERT(width % size == 0);
		Log.ASSERT(height % size == 0);

		Vec2i vec = new Vec2i(width, height).div(size);
		Vec2Iterator iter = new Vec2Iterator(new Vec2i(), vec);
		while(iter.hasNext())
		{
			Vec2i vec1 = iter.next();
			this.maps.put(vec1, new IntMap(size, size));
		}
	}

	@Override
	public boolean contains(int beginX, int beginY, int endX, int endY, int value)
	{
		Log.ASSERT(beginX < endX);
		Log.ASSERT(beginY < endY);
		Log.ASSERT(this.isValid(beginX, beginY) && this.isValid(endX, endY));

		for(int i = beginX; i < endX; ++i)
		{
			for(int j = beginY; j < endY; ++j)
			{
				if (this.isValue(i, j, value))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isValue(int x, int y, int value)
	{
		Log.ASSERT(this.isValid(x, y));

		return this.get(x, y) == value;
	}

	@Override
	public boolean isValid(int x, int y)
	{
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	@Override
	public void fill(int value)
	{
		for(int i = 0; i < this.size(); ++i)
		{
			this.set(i, value);
		}
	}

	@Override
	public void fill(int beginX, int beginY, int endX, int endY, int value)
	{
		Log.ASSERT(beginX < endX);
		Log.ASSERT(beginY < endY);
		Log.ASSERT(this.isValid(beginX, beginY) && this.isValid(endX, endY));

		for(int i = beginX; i < endX; ++i)
		{
			for(int j = beginY; j < endY; ++j)
			{
				this.set(i, j, value);
			}
		}
	}

	@Override
	public void set(int x, int y, int value)
	{
		Log.ASSERT(this.isValid(x, y));

		Vec2i vec = new Vec2i(x, y);
		IntMap map = this.maps.get(vec.div(this.size));

		Log.ASSERT(map != null);

		Vec2i vec1 = vec.copy();
		vec1.x %= this.size;
		vec1.y %= this.size;
		map.set(vec1.x, vec1.y, value);
	}

	@Override
	public int get(int x, int y)
	{
		Log.ASSERT(this.isValid(x, y));

		Vec2i vec = new Vec2i(x, y);
		IntMap map = this.maps.get(vec.div(this.size));

		Log.ASSERT(map != null);

		Vec2i vec1 = vec.copy();
		vec1.x %= this.size;
		vec1.y %= this.size;
		return map.get(vec1.x, vec1.y);
	}

	@Override
	public void set(int index, int value)
	{
		Vec2i vec = new Vec2i(index % this.width, index / this.width);
		this.set(vec.x, vec.y, value);
	}

	@Override
	public int get(int index)
	{
		Vec2i vec = new Vec2i(index % this.width, index / this.width);
		return this.get(vec.x, vec.y);
	}

	@Override
	public int indexOf(int x, int y)
	{
		Log.ASSERT(this.isValid(x, y));

		return x + y * this.width;
	}

	@Override
	public int size()
	{
		return this.width * this.height;
	}

	@Override
	public int[] toIntArray()
	{
		int[] result = new int[this.size()];
		Vec2Iterator iter = new Vec2Iterator(new Vec2i(), new Vec2i(this.width - 1, this.height - 1));
		int i = 0;
		while(iter.hasNext())
		{
			Vec2i vec = iter.next();
			int j = this.get(vec.x, vec.y);
			result[i++] = j;
		}

		return result;
	}

	public IntMap[] getMaps()
	{
		IntMap[] result = new IntMap[this.size() / (this.size * this.size)];
		Vec2Iterator iter = new Vec2Iterator(new Vec2i(), new Vec2i((this.width / this.size) - 1, (this.height / this.size) - 1));
		int i = 0;
		while(iter.hasNext())
		{
			Vec2i vec = iter.next();
			IntMap map = this.maps.get(vec);
			result[i++] = map;
		}

		return result;
	}
}
