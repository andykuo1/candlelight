package net.jimboi.mod.torchlite;

import org.bstone.util.direction.Direction;
import org.joml.Vector2i;

import java.util.Random;

public class WorldGenMazeSparse extends AbstractWorldGen
{
	protected final int tile;

	public WorldGenMazeSparse(int tile)
	{
		this.tile = tile;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		boolean running = true;

		while(running)
		{
			running = false;

			Recti rect = new Recti(new Vec2i(), new Vec2i(world.map.width - 1, world.map.height - 1)).inflate(-1);
			Vec2Iterator iter = new Vec2Iterator(rect);
			while(iter.hasNext())
			{
				Vec2i pos = iter.next();
				if (this.isSolid(world.map.get(pos.x, pos.y))) continue;

				int exits = 0;
				for(Direction dir : Direction.Cardinals.values())
				{
					Vector2i dv = dir.polari(new Vector2i());
					Vec2i vec = new Vec2i(dv.x, dv.y);
					if (!this.isSolid(world.map.get(pos.x + vec.x, pos.y + vec.y))) exits++;
				}

				if (exits != 1) continue;

				world.map.set(pos.x, pos.y, this.tile);
				running = true;
			}
		}

		return true;
	}

	public boolean isSolid(int tile)
	{
		return this.tile == tile;
	}

	@Override
	public int getWeight()
	{
		return 0;
	}

}
