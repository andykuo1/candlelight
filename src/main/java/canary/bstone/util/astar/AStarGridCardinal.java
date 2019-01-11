package canary.bstone.util.astar;

import canary.bstone.util.grid.GridMap;
import org.joml.Vector2i;

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
		this.solid = solid;
		this.map = map;
	}

	@Override
	protected Integer getFScore(Integer value, Integer other)
	{
		return value + other;
	}

	@Override
	protected Integer getGScore(Integer node, Integer other)
	{
		return !node.equals(other) ? 1 : 0;
	}

	@Override
	protected Integer getHScore(Integer node, Integer end)
	{
		int w = map.width();
		int x = node % w;
		int y = node / w;
		int x2 = end % w;
		int y2 = end / w;
		int dx = x - x2;
		int dy = y - y2;
		return dx * dx + dy * dy;
	}

	@Override
	protected Iterable<Integer> getAvailableNodes(Integer node)
	{
		List<Integer> neighbors = new ArrayList<>(4);
		int w = map.width();
		int x = node % w;
		int y = node / w;
		Vector2i key = new Vector2i(x, y);
		if (x < w - 1 && solid.test(map.get(key.set(x + 1, y)))) neighbors.add(node + 1);
		if (x > 0 && solid.test(map.get(key.set(x - 1, y)))) neighbors.add(node - 1);
		if (y < w - 1 && solid.test(map.get(key.set(x, y + 1)))) neighbors.add(node + w);
		if (y > 0 && solid.test(map.get(key.set(x, y - 1)))) neighbors.add(node - w);
		return neighbors;
	}

	@Override
	protected boolean isEndNode(Integer node, Integer end)
	{
		return node.equals(end);
	}

	public GridMap<T> getMap()
	{
		return this.map;
	}
}
