package org.qsilver.astar;

public final class HeuristicMap<A>
{
	private final int[] heuristic;
	private final A toNode;

	public HeuristicMap(int[] map, A to)
	{
		this.heuristic = map;
		this.toNode = to;
	}

	public int getNodeHeuristic(int index)
	{
		assert (index >= 0 && index < this.heuristic.length);

		return this.heuristic[index];
	}

	public A getNodeTo()
	{
		return this.toNode;
	}
}