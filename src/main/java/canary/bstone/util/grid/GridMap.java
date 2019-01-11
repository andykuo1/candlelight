package canary.bstone.util.grid;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Created by Andy on 11/13/17.
 */
public abstract class GridMap<T>
{
	protected final int width;
	protected final int height;

	public GridMap(int width, int height)
	{
		if (width < 0 || height < 0)
		{
			throw new IllegalArgumentException("map width and height must be non-negative");
		}

		this.width = width;
		this.height = height;
	}

	public abstract T set(Vector2ic key, T value);
	public abstract T get(Vector2ic key);

	public void clear(T defaultValue)
	{
		this.forEach((vector2ic, t) -> this.set(vector2ic, defaultValue));
	}

	public void forEach(BiConsumer<Vector2ic, T> action)
	{
		this.forEach(0, 0, this.width, this.height, action);
	}

	public void forEach(int x, int y, int w, int h, BiConsumer<Vector2ic, T> action)
	{
		Vector2i key = new Vector2i();
		for(int i = 0; i < w; ++i)
		{
			for(int j = 0; j < h; ++j)
			{
				key.set(x + i, y + j);
				action.accept(key, this.get(key));
			}
		}
	}

	public Vector2ic find(BiFunction<Vector2ic, T, Boolean> test)
	{
		return this.find(0, 0, this.width, this.height, test);
	}

	public Vector2ic find(int x, int y, int w, int h, BiFunction<Vector2ic, T, Boolean> test)
	{
		Vector2i key = new Vector2i();
		for(int i = 0; i < w; ++i)
		{
			for(int j = 0; j < h; ++j)
			{
				key.set(x + i, y + j);
				Boolean flag = test.apply(key, this.get(key));
				if (flag != null && flag)
				{
					return key;
				}
			}
		}
		return null;
	}

	public int width()
	{
		return this.width;
	}

	public int height()
	{
		return this.height;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append('[');

		final Vector2i key = new Vector2i();
		for(int y = 0; y < this.height; ++y)
		{
			if (y > 0) sb.append('\n');

			for(int x = 0; x < this.width; ++x)
			{
				key.set(x, y);
				sb.append(this.get(key));

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
