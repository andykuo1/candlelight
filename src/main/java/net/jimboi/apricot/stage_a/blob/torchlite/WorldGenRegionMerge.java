package net.jimboi.apricot.stage_a.blob.torchlite;

import org.bstone.util.direction.Direction;
import org.bstone.util.small.SmallMap;
import org.bstone.util.small.SmallSet;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WorldGenRegionMerge extends AbstractWorldGen
{
	protected final Predicate<Integer> isCarveableTile;
	protected final Supplier<Integer> getConnectorTiles;
	protected final float mergePercent;

	public WorldGenRegionMerge(Predicate<Integer> isCarveableTile, Supplier<Integer> getConnectorTiles, float mergePercent)
	{
		this.isCarveableTile = isCarveableTile;
		this.getConnectorTiles = getConnectorTiles;
		this.mergePercent = mergePercent;
	}

	@Override
	public boolean generate(final Random rand, final WorldGenData world)
	{
		final Map<Vec2i, Set<Integer>> connections = new SmallMap<Vec2i, Set<Integer>>();

		Recti rect = new Recti(new Vec2i(), new Vec2i(world.map.width - 1, world.map.height - 1)).inflate(-1);
		Vec2Iterator iter = new Vec2Iterator(rect);
		while(iter.hasNext())
		{
			Vec2i pos = iter.next();
			if (!this.isCarveableTile.test(world.map.get(pos.x, pos.y))) continue;

			Set<Integer> posRegions = new SmallSet<Integer>();
			for(Direction dir : Direction.Cardinals.values())
			{
				Vector2i dv = dir.polari(new Vector2i());
				Vec2i vec = new Vec2i(dv.x, dv.y);
				int region = world.region.get(pos.x + vec.x, pos.y + vec.y);
				if (region != 0) posRegions.add(region);
			}

			if (posRegions.size() < 2) continue;

			connections.put(pos, posRegions);
		}

		if (connections.isEmpty()) return false;

		List<Vec2i> connectors = new ArrayList<Vec2i>();
		connectors.addAll(connections.keySet());
		Collections.shuffle(connectors, rand);

		final int[] merged = new int[world.regionIndex + 1];
		Set<Integer> openRegions = new SmallSet<Integer>();

		for(int i = 0; i <= world.regionIndex; ++i)
		{
			merged[i] = i;
			openRegions.add(i);
		}

		while(openRegions.size() > 1)
		{
			if (connectors.isEmpty()) break;
			final Vec2i connector = connectors.get(0);

			world.map.set(connector.x, connector.y, this.getConnectorTiles.get());
			///genMapCarveJunction(rand, map, connector);

			List<Integer> connectorRegions = new ArrayList<Integer>();
			for(Integer region : connections.get(connector))
			{
				connectorRegions.add(merged[region]);
			}

			Iterator<Integer> connectorRegionsIter = connectorRegions.iterator();
			Integer dest = connectorRegionsIter.next();
			List<Integer> srcs = new ArrayList<Integer>();
			while(connectorRegionsIter.hasNext())
			{
				srcs.add(connectorRegionsIter.next());
			}

			for(int i = 0; i <= world.regionIndex; ++i)
			{
				if (srcs.contains(merged[i]))
				{
					merged[i] = dest;
				}
			}

			openRegions.removeAll(srcs);

			connectors.removeIf(new Predicate<Vec2i>(){
				@Override
				public boolean test(Vec2i pos)
				{
					if (connector.sub(pos).length() < 2) return true;

					Set<Integer> posRegions = new SmallSet<Integer>();
					for(Integer region : connections.get(pos))
					{
						posRegions.add(merged[region]);
					}

					if (posRegions.size() > 1) return false;

					if (rand.nextFloat() < mergePercent)
					{
						world.map.set(pos.x, pos.y, getConnectorTiles.get());
						///genMapCarveJunction(rand, map, pos);
					}

					return true;
				}
			});
		}

		return true;
	}

	@Override
	public int getWeight()
	{
		return 0;
	}
}
