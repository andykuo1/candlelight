package net.jimboi.torchlite;

import java.util.Random;

public class WorldGenGrid extends AbstractWorldGen
{
	protected final int gridTile;
	protected final int gridSize;
	protected boolean alternate = true;

	public WorldGenGrid(int gridTile, int gridSize)
	{
		this.gridTile = gridTile;
		this.gridSize = gridSize;
	}

	public WorldGenGrid setAlternate(boolean alternate)
	{
		this.alternate = alternate;
		return this;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		for(int i = 0; i < world.map.width; i += this.gridSize)
		{
			for(int j = 0; j < world.map.height; j += this.gridSize)
			{
				for(int k = 0; k < this.gridSize; ++k)
				{
					world.map.set(i + k, j, this.gridTile);
					if (this.alternate) ++k;
				}

				for(int k = 0; k < this.gridSize; ++k)
				{
					world.map.set(i, j + k, this.gridTile);
					if (this.alternate) ++k;
				}
			}
		}

		return true;
	}

	@Override
	public int getWeight()
	{
		return 0;
	}
}
