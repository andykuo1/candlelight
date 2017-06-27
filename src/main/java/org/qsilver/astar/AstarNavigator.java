package org.qsilver.astar;

import org.bstone.util.small.SmallSet;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public final class AstarNavigator<A>
{
	private final NavigatorMap<A> map;

	private FunctionNavigationCost<A> costFunction;

	/**
	 * Pass null for function if cost will always be 0
	 */
	public AstarNavigator(NavigatorMap<A> map, FunctionNavigationCost<A> costFunction)
	{
		if (map == null)
		{
			throw new IllegalArgumentException();
		}

		this.map = map;
		this.costFunction = costFunction;
	}

	public AstarNavigator(NavigatorMap<A> map)
	{
		this(map, null);
	}

	public AstarNavigator<A> setNavigationCostFunction(FunctionNavigationCost<A> costFunction)
	{
		this.costFunction = costFunction;
		return this;
	}

	public Stack<A> navigate(A start, A end)
	{
		if (start == null || end == null)
		{
			throw new IllegalArgumentException();
		}

		Stack<Integer> nodes = navigate(this.costFunction, this.map, this.map.getNodeIndexOf(start), this.map.getNodeIndexOf(end));
		Stack<A> result = new Stack<>();
		if (nodes == null) return result;

		for (Integer node : nodes)
		{
			result.add(this.map.getNodeObjectOf(node));
		}

		return result;
	}

	/**
	 * Gets an ordered stack of nodes leading from the passed-in start node to the end node;
	 * <br>
	 * <br>Will NOT ensure index is valid!
	 */
	protected static <A> Stack<Integer> navigate(FunctionNavigationCost<A> costFunction, NavigatorMap<A> map, int start, int end)
	{
		if (map == null || start < 0 || end < 0)
		{
			throw new IllegalArgumentException();
		}

		Stack<Integer> result = new Stack<>();

		HeuristicMap<A> hmap = map.getNodeHeuristicMap(end);
		WeightMap gmap = new WeightMap(map.size());
		Set<Integer> openSet = new SmallSet<>();
		Set<Integer> closeSet = new SmallSet<>();

		int node = start;
		int[] nodeNeighbors = map.getNodeNeighbors(node);

		if (nodeNeighbors == null)
		{
			throw new IllegalStateException();
		}

		closeSet.add(node);

		//while node is still valid and the node's neighbors are not the end node
		while (node != -1 && indexOf(nodeNeighbors, end) == -1)
		{
			for (int i : nodeNeighbors)
			{
				//if neighbor happens to be null
				//then find another
				if (i == -1) continue;

				int neighbor = i;

				//if neighbor is not validated
				if (!closeSet.contains(neighbor))
				{
					//cost to move from node to neighbor
					int costSum = gmap.cost(node) + (costFunction != null ? costFunction.apply(map, map.getNodeObjectOf(node), map.getNodeObjectOf(neighbor)) : 0);

					//if neighbor is not checked
					if (!openSet.contains(neighbor))
					{
						openSet.add(neighbor);
						gmap.setWeight(neighbor, costSum, node);
					}
					else if (costSum < gmap.cost(neighbor))
					{
						//if cost to neighbor is lower than stored value,
						//since path should be optimal,
						//then re-parent this node to that
						gmap.setWeight(neighbor, gmap.cost(neighbor), node);
					}
				}
			}

			//if could not reach node with a neighbor that is equal to the passed-in end node,
			//since there are no more nodes,
			//then fail
			if (openSet.size() == 0) return null;

			//find node with lowest score and restart calculation with this
			Iterator<Integer> iter = openSet.iterator();
			int lowest = iter.next();
			while (iter.hasNext())
			{
				int i = iter.next();
				int f1 = hmap.getNodeHeuristic(i) + gmap.cost(i);
				int f2 = hmap.getNodeHeuristic(lowest) + gmap.cost(lowest);
				if (f1 < f2)
				{
					lowest = i;
				}
			}
			node = lowest;
			nodeNeighbors = map.getNodeNeighbors(node);

			openSet.remove(node);
			closeSet.add(node);

			if (node == -1) break;
		}

		//backtrack through parent nodes and put them in result
		result.push(end);
		while (node != start && node != -1)
		{
			result.push(node);
			node = gmap.parent(node);
		}
		result.push(start);

		return result;
	}

	/**
	 * Class to stop node data by index; <br>Used only by {@link AstarNavigator#navigate(FunctionNavigationCost,
	 * NavigatorMap, int, int)}
	 */
	private static final class WeightMap
	{
		private final int[] costs, parents;

		private WeightMap(int size)
		{
			this.costs = new int[size];
			this.parents = new int[size];
		}

		private final int cost(int index)
		{
			assert (index >= 0 && index < this.costs.length);

			return this.costs[index];
		}

		private final int parent(int index)
		{
			assert (index >= 0 && index < this.parents.length);

			return this.parents[index];
		}

		private final void setWeight(int index, int cost, int parent)
		{
			assert (index >= 0 && index < this.costs.length);

			this.costs[index] = cost;
			this.parents[index] = parent;
		}
	}

	private static int indexOf(int[] array, int value)
	{
		for (int i = 0; i < array.length; ++i)
		{
			if (value == array[i])
			{
				return i;
			}
		}

		return -1;
	}
}
