package org.bstone.util.astar;

import org.bstone.util.gridmap.GridMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Andy on 9/26/17.
 */
public class AStarGridCardinal<T> extends AStar<Integer, Integer>
{
	protected final Predicate<T> solid;
	protected final GridMap<T> map;

	public AStarGridCardinal(GridMap<T> map, Predicate<T> solid)
	{
		super(
				(c, c2) -> c + c2,
				(integer, integer2) -> !integer.equals(integer2) ? 1 : 0,
				(integer, integer2) -> {
					int w = map.getWidth();
					int x = integer % w;
					int y = integer / w;
					int x2 = integer2 % w;
					int y2 = integer2 / w;
					int dx = x - x2;
					int dy = y - y2;
					return dx * dx + dy * dy;
				},
				integer -> {
					List<Integer> neighbors = new ArrayList<>(4);
					int w = map.getWidth();
					int x = integer % w;
					int y = integer / w;
					if (x < w - 1 && solid.test(map.getValue(integer + 1))) neighbors.add(integer + 1);
					if (x > 0 && solid.test(map.getValue(integer - 1))) neighbors.add(integer - 1);
					if (y < w - 1 && solid.test(map.getValue(integer + w))) neighbors.add(integer + w);
					if (y > 0 && solid.test(map.getValue(integer - w))) neighbors.add(integer - w);
					return neighbors;
				},
				Integer::equals);

		this.solid = solid;
		this.map = map;
	}

	public GridMap<T> getMap()
	{
		return this.map;
	}
}
