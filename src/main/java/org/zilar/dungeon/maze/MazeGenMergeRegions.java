package org.zilar.dungeon.maze;

import org.bstone.util.Direction;
import org.bstone.util.small.SmallMap;
import org.bstone.util.small.SmallSet;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.zilar.dungeon.DungeonData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Andy on 5/29/17.
 */
public class MazeGenMergeRegions extends AbstractMazeGen
{
	protected final Random rand;

	protected final RegionHandler regionHandler;

	protected final float mergePercent;

	protected final Predicate<Integer> isCarveableTile;
	protected final Supplier<Integer> getConnectorTiles;

	public MazeGenMergeRegions(Random rand, RegionHandler regionHandler, float mergePercent, Predicate<Integer> isCarveableTile, Supplier<Integer> getConnectorTiles)
	{
		this.rand = rand;

		this.regionHandler = regionHandler;

		this.mergePercent = mergePercent;
		this.isCarveableTile = isCarveableTile;
		this.getConnectorTiles = getConnectorTiles;
	}

	@Override
	public void bake(DungeonData data)
	{
		final Map<Vector2i, Set<Integer>> connections = new SmallMap<>();

		for (int x = 1; x < data.width - 1; ++x)
		{
			for (int y = 1; y < data.height - 1; ++y)
			{
				if (!this.isCarveableTile.test(data.getTiles().get(x, y))) continue;

				Set<Integer> posRegions = new SmallSet<>();
				for (Direction dir : Direction.Cardinals.values())
				{
					Vector2i dv = dir.polari(new Vector2i());
					int region = data.getRegions().get(x + dv.x, y + dv.y);
					if (region != 0) posRegions.add(region);
				}

				if (posRegions.size() < 2) continue;

				connections.put(new Vector2i(x, y), posRegions);
			}
		}

		if (connections.isEmpty()) return;

		List<Vector2i> connectors = new ArrayList<>();
		connectors.addAll(connections.keySet());
		Collections.shuffle(connectors, rand);

		final int[] merged = new int[this.regionHandler.size()];
		Set<Integer> openRegions = new SmallSet<>();

		for (int i = 0; i < this.regionHandler.size(); ++i)
		{
			merged[i] = i;
			openRegions.add(i);
		}

		while (openRegions.size() > 1)
		{
			if (connectors.isEmpty()) break;
			final Vector2ic connector = connectors.get(0);

			data.getTiles().put(connector.x(), connector.y(), this.getConnectorTiles.get());
			///genMapCarveJunction(rand, map, connector);

			List<Integer> connectorRegions = new ArrayList<>();
			for (Integer region : connections.get(connector))
			{
				connectorRegions.add(merged[region]);
			}

			Iterator<Integer> connectorRegionsIter = connectorRegions.iterator();
			Integer dest = connectorRegionsIter.next();
			List<Integer> srcs = new ArrayList<>();
			while (connectorRegionsIter.hasNext())
			{
				srcs.add(connectorRegionsIter.next());
			}

			for (int i = 0; i < this.regionHandler.size(); ++i)
			{
				if (srcs.contains(merged[i]))
				{
					merged[i] = dest;
				}
			}

			openRegions.removeAll(srcs);

			connectors.removeIf((pos) ->
			{
				if (connector.sub(pos, new Vector2i()).length() < 2) return true;

				Set<Integer> posRegions = new SmallSet<>();
				for (Integer region : connections.get(pos))
				{
					posRegions.add(merged[region]);
				}

				if (posRegions.size() > 1) return false;

				if (rand.nextFloat() < mergePercent)
				{
					data.getTiles().put(pos.x, pos.y, getConnectorTiles.get());
					///genMapCarveJunction(rand, map, pos);
				}

				return true;
			});
		}
	}

	@Override
	public void clear()
	{
	}
}
