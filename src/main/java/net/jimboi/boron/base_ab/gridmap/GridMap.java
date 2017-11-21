package net.jimboi.boron.base_ab.gridmap;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.function.BiConsumer;

/**
 * Created by Andy on 9/13/17.
 */
public abstract class GridMap<T>
{
	protected final int width;
	protected final int height;
	protected final int length;

	public GridMap(int width, int height)
	{
		if (width <= 0 || height <= 0)
		{
			throw new IllegalArgumentException("map width and height must be greater than 0");
		}

		this.width = width;
		this.height = height;
		this.length = this.width * this.height;
	}

	public final boolean containsKey(int x, int y)
	{
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}

	public final boolean containsValue(T value)
	{
		for(int i = 0; i < this.length; ++i)
		{
			if (this.getValue(i).equals(value))
			{
				return true;
			}
		}

		return false;
	}

	public abstract T setValue(int index, T value);

	public final T setValue(int x, int y, T value)
	{
		return this.setValue(x + y * this.width, value);
	}

	public abstract T getValue(int index);

	public final T getValue(int x, int y)
	{
		return this.getValue(x + y * this.width);
	}

	public final Vector2i getKey(int index, Vector2i dst)
	{
		return dst.set(index % this.width, index / this.width);
	}

	public final int indexOf(int x, int y)
	{
		return x + y * this.width;
	}

	public final void forEach(int x, int y, int width, int height, BiConsumer<Vector2ic, T> action)
	{
		final Vector2i vec = new Vector2i();
		for(int i = 0; i < width; ++i)
		{
			for(int j = 0; j < height; ++j)
			{
				vec.set(x + i, y + j);
				action.accept(vec, this.getValue(vec.x, vec.y));
			}
		}
	}

	public final void clear(T defaultValue)
	{
		for(int i = 0; i < this.length; ++i)
		{
			this.setValue(i, defaultValue);
		}
	}

	public final int getWidth()
	{
		return this.width;
	}

	public final int getHeight()
	{
		return this.height;
	}

	public final int length()
	{
		return this.length;
	}

	@Override
	public final String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');

		boolean flag = false;
		for(int y = 0; y < this.height; ++y)
		{
			if (y > 0)
			{
				sb.append('\n');
			}

			for(int x = 0; x < this.width; ++x)
			{
				sb.append(this.getValue(x, y));

				if (!(x == this.width - 1 && y == this.height - 1))
				{
					sb.append(',');
					sb.append(' ');
				}
			}
		}
		sb.append(']');
		return sb.toString();
	}
}
