package net.jimboi.apricot.base.astar;

import org.bstone.util.small.SmallMap;

public final class HeuristicMapMaker<A>
{
	/**
	 * Calculated heuristic maps to save computation time;
	 * <br>Should be cleared if map is changed!
	 */
	protected final SmallMap<A, int[]> cachedHeuristicMaps = new SmallMap<>();
	private boolean useCache;

	private FunctionHeuristicCost<A> costFunction;

	/**
	 * Pass null if heuristics is always 0
	 */
	public HeuristicMapMaker(FunctionHeuristicCost<A> costFunction)
	{
		this.costFunction = costFunction;
	}

	public HeuristicMapMaker()
	{
		this(null);
	}

	/**
	 * Save calculated heuristic maps to save computation time;
	 * <br>Should be cleared if map is changed!
	 */
	public HeuristicMapMaker<A> setUseCache(boolean useCache)
	{
		this.useCache = useCache;
		return this;
	}


	/**
	 * Calculates heuristic value for map
	 * <br>Should be cleared if function is changed!
	 */
	public HeuristicMapMaker<A> setCostFunction(FunctionHeuristicCost<A> costFunction)
	{
		this.costFunction = costFunction;
		return this;
	}

	public HeuristicMap<A> createMap(NavigatorMap<A> map, int to)
	{
		int[] hmap;
		A toNode = map.getNodeObjectOf(to);

		if (this.useCache && this.cachedHeuristicMaps.containsKey(toNode))
		{
			hmap = this.cachedHeuristicMaps.get(toNode);
		}
		else
		{
			hmap = new int[map.size()];

			for (int i = 0; i < hmap.length; ++i)
			{
				A node = map.getNodeObjectOf(i);
				int h = this.costFunction != null ? this.costFunction.apply(map, node, toNode) : 0;
				hmap[i] = h;
			}

			this.cachedHeuristicMaps.put(toNode, hmap);
		}

		return new HeuristicMap<>(hmap, toNode);
	}

	public void clearCache()
	{
		this.cachedHeuristicMaps.clear();
	}
}
