package canary.zilar.dungeon;

import java.util.Random;

/**
 * Created by Andy on 5/28/17.
 */
public abstract class DungeonBuilder
{
	protected final Random rand = new Random();
	protected final int width;
	protected final int height;

	public DungeonBuilder(long seed, int width, int height)
	{
		this.rand.setSeed(seed);

		this.width = width;
		this.height = height;
	}

	public abstract DungeonData bake();

	public final Random getRandom()
	{
		return this.rand;
	}

	public final int getWidth()
	{
		return this.width;
	}

	public final int getHeight()
	{
		return this.height;
	}
}
