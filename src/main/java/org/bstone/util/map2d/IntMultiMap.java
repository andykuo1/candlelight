package org.bstone.util.map2d;

import org.bstone.util.poma.Poma;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.HashMap;
import java.util.Map;

public class IntMultiMap extends IntMap
{
	private final Map<Vector2i, IntMap> maps = new HashMap<>();
	private final Vector2i mapsIndex = new Vector2i();
	private final int chunkSize;

	public IntMultiMap(int width, int height, int chunkSize)
	{
		super(width, height);

		this.chunkSize = chunkSize;

		Poma.ASSERT(width % this.chunkSize == 0);
		Poma.ASSERT(height % this.chunkSize == 0);

		Vector2ic vec = new Vector2i(width / this.chunkSize, height / this.chunkSize);
		for(int i = 0; i < vec.x(); ++i)
		{
			for(int j = 0; j < vec.y(); ++j)
			{
				this.maps.put(new Vector2i(i, j), new IntMap(this.chunkSize, this.chunkSize));
			}
		}
	}

	@Override
	public void fill(int value)
	{
		for(IntMap map : this.maps.values())
		{
			map.fill(value);
		}
	}

	@Override
	public void set(int x, int y, int value)
	{
		Poma.ASSERT(this.isValid(x, y));

		getMap(x, y).set(x % this.chunkSize, y % this.chunkSize, value);
	}

	@Override
	public int get(int x, int y)
	{
		Poma.ASSERT(this.isValid(x, y), new Vector2i(x, y));

		return getMap(x, y).get(x % this.chunkSize, y % this.chunkSize);
	}

	protected IntMap getMap(int x, int y)
	{
		Poma.ASSERT(this.isValid(x, y));

		return this.maps.get(this.mapsIndex.set(x / this.chunkSize, y / this.chunkSize));
	}
}
