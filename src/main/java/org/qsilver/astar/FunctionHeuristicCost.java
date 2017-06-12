package org.qsilver.astar;

/**
 * Gets heuristic value for map for passed-in start and end nodes
 */
@FunctionalInterface
public interface FunctionHeuristicCost<A>
{
	int apply(NavigatorMap<A> map, A from, A to);
}
