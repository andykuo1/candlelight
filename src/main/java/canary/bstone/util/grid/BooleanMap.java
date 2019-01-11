package canary.bstone.util.grid;

import org.joml.Vector2ic;

/**
 * Created by Andy on 11/13/17.
 */
public class BooleanMap extends GridMap<Boolean>
{
	private final boolean[] array;

	public BooleanMap(int width, int height)
	{
		super(width, height);

		this.array = new boolean[this.width * this.height];
	}

	@Override
	public Boolean set(Vector2ic key, Boolean value)
	{
		return this.set(key.x(), key.y(), value);
	}

	@Override
	public Boolean get(Vector2ic key)
	{
		return this.get(key.x(), key.y());
	}

	public boolean set(int x, int y, boolean value)
	{
		int i = x + y * this.width;
		boolean flag = this.array[i];
		this.array[i] = value;
		return flag;
	}

	public void toggle(int x, int y)
	{
		int i = x + y * this.width;
		this.array[i] = !this.array[i];
	}

	public boolean get(int x, int y)
	{
		return this.array[x + y * this.width];
	}

	public boolean[] array()
	{
		return this.array;
	}
}
