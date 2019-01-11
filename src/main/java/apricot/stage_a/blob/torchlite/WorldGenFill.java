package apricot.stage_a.blob.torchlite;

import java.util.Random;

public class WorldGenFill extends AbstractWorldGen
{
	protected final int tile;

	public WorldGenFill(int tile)
	{
		this.tile = tile;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		world.map.fill(tile);
		world.region.fill(0);
		world.regionIndex = 1;

		return true;
	}

	@Override
	public int getWeight()
	{
		return Integer.MIN_VALUE;
	}
}
