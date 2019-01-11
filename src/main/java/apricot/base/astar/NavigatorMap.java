package apricot.base.astar;

@Deprecated
public abstract class NavigatorMap<A>
{
	/**
	 * Gets only reachable nodes from passed-in index;
	 * <br>If the returned array CANNOT contain invalid indices, but can contain -1 as null
	 */
	protected abstract int[] getNodeNeighbors(int index);

	/**
	 * Gets heuristic map for passed-in start and end nodes
	 */
	protected abstract HeuristicMap<A> getNodeHeuristicMap(int to);

	/**
	 * Gets index of passed-in node object;
	 * <br>Used only by {@link AstarNavigator} to parse node object arguments from the caller;
	 * <br>Must be a UNIQUE index per node object in the map
	 */
	protected abstract int getNodeIndexOf(A node);

	/**
	 * Gets node object of passed-in index;
	 * <br>Used only by {@link AstarNavigator} to return readable node stack to caller
	 */
	protected abstract A getNodeObjectOf(int index);

	protected abstract int size();
}
