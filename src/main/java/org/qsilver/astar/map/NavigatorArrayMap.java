package org.qsilver.astar.map;

import org.qsilver.astar.HeuristicMap;
import org.qsilver.astar.HeuristicMapMaker;
import org.qsilver.astar.NavigatorMap;

public abstract class NavigatorArrayMap<A> extends NavigatorMap<A>
{
	protected final A[] map;

	protected HeuristicMapMaker<A> mapMaker;

	public NavigatorArrayMap(A[] map)
	{
		this.map = map;

		this.mapMaker = new HeuristicMapMaker<>((m, from, to) -> ((NavigatorArrayMap<A>) m).getNodeHeuristicTo(from, to));
	}

	@Override
	protected abstract int[] getNodeNeighbors(int parIndex);

	public abstract int getNodeHeuristicTo(A from, A to);

	@Override
	protected HeuristicMap<A> getNodeHeuristicMap(int to)
	{
		return this.mapMaker.createMap(this, to);
	}

	@Override
	protected abstract int getNodeIndexOf(A node);

	@Override
	protected abstract A getNodeObjectOf(int index);

	@Override
	protected int size()
	{
		return this.map.length;
	}
}
