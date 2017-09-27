package net.jimboi.apricot.base.astar;

/**
 * Gets numerical value of difficulty to move from passed-in index to the other;
 * <br>Does not factor in the path, only the cost of change in position;
 * <br>Returned value should be independent of previous values
 */
@FunctionalInterface
public interface FunctionNavigationCost<A>
{
	int apply(NavigatorMap<A> map, A from, A to);
}
