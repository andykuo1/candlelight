package net.jimboi.stage_b.gnome.dungeon.maze;

import org.bstone.util.direction.Direction;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.qsilver.util.map2d.IntMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Andy on 5/29/17.
 */
public class CorridorBuilder
{
	public static int EMPTY_TILE = 0;
	public static int CORRIDOR_TILE = 1;

	public void carve(Random rand, IntMap src, IntMap dst, int x, int y, float twistPercent, Predicate<Integer> isCarveableTile)
	{
		Deque<Vector2i> cells = new ArrayDeque<>();
		Direction prevDir = null;

		dst.set(x, y, CORRIDOR_TILE);

		cells.add(new Vector2i(x, y));
		while (!cells.isEmpty())
		{
			final Vector2ic cell = cells.getLast();
			final Vector2i dv = new Vector2i();

			List<Direction> open = new ArrayList<>();
			for (Direction dir : Direction.Cardinals.values())
			{
				dir.polari(dv);
				if (src.isValid(cell.x() + dv.x() * 3, cell.y() + dv.y() * 3))
				{
					int i = cell.x() + dv.x() * 2;
					int j = cell.y() + dv.y() * 2;
					if (isCarveableTile.test(src.get(i, j)) && dst.get(i, j) == EMPTY_TILE)
					{
						open.add(dir);
					}
				}
			}

			if (!open.isEmpty())
			{
				Direction dir;

				if (open.contains(prevDir) && rand.nextFloat() > twistPercent)
				{
					dir = prevDir;
				}
				else
				{
					dir = open.get(rand.nextInt(open.size()));
				}

				dir.polari(dv);
				int xx = cell.x() + dv.x();
				int yy = cell.y() + dv.y();
				dst.set(xx, yy, CORRIDOR_TILE);
				xx += dv.x();
				yy += dv.y();
				dst.set(xx, yy, CORRIDOR_TILE);

				cells.add(new Vector2i(xx, yy));
				prevDir = dir;
			}
			else
			{
				cells.removeLast();
				prevDir = null;
			}
		}
	}
}
