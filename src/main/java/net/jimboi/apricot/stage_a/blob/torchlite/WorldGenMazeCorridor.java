package net.jimboi.apricot.stage_a.blob.torchlite;

import org.bstone.util.Direction;
import org.joml.Vector2i;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class WorldGenMazeCorridor extends AbstractWorldGen
{
	protected final Predicate<Integer> isCarveableTile;
	protected final int floorTile;
	protected final int region;
	protected final Vec2i pos;
	protected final float twistPercent;

	public WorldGenMazeCorridor(Predicate<Integer> isSolidTile, int floorTile, int region, Vec2i pos, float twistPercent)
	{
		this.isCarveableTile = isSolidTile;
		this.floorTile = floorTile;
		this.region = region;
		this.pos = pos;
		this.twistPercent = twistPercent;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		Deque<Vec2i> cells = new ArrayDeque<Vec2i>();
		Direction prevDir = null;

		world.map.set(this.pos.x, this.pos.y, this.floorTile);
		world.region.set(this.pos.x, this.pos.y, this.region);

		cells.add(this.pos);
		while(!cells.isEmpty())
		{
			Vec2i cell = cells.getLast();

			List<Direction> open = new ArrayList<Direction>();
			for(Direction dir : Direction.Cardinals.values())
			{
				Vector2i dv = dir.polari(new Vector2i());
				Vec2i vec = new Vec2i(dv.x, dv.y);
				if (world.map.isValid(cell.x + vec.x * 3, cell.y + vec.y * 3))
				{
					if (this.isCarveableTile.test(world.map.get(cell.x + vec.x * 2, cell.y + vec.y * 2)))
					{
						open.add(dir);
					}
				}
			}

			if (!open.isEmpty())
			{
				Direction dir;

				if (open.contains(prevDir) && rand.nextFloat() > this.twistPercent)
				{
					dir = prevDir;
				}
				else
				{
					dir = open.get(rand.nextInt(open.size()));
				}

				Vector2i dv = dir.polari(new Vector2i());
				Vec2i vec = new Vec2i(dv.x, dv.y);

				Vec2i vec1 = cell.add(vec);
				world.map.set(vec1.x, vec1.y, this.floorTile);
				world.region.set(vec1.x, vec1.y, this.region);

				Vec2i vec2 = cell.add(vec.mul(2));
				world.map.set(vec2.x, vec2.y, this.floorTile);
				world.region.set(vec2.x, vec2.y, this.region);

				cells.add(cell.add(vec.mul(2)));
				prevDir = dir;
			}
			else
			{
				cells.removeLast();
				prevDir = null;
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
