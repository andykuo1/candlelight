package canary.bstone.util.astar;

import canary.bstone.util.small.SmallMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Andy on 9/26/17.
 */
public abstract class AStar<N, C extends Comparable<C>>
{
	private final Queue<N> opened = new LinkedList<>();
	private final Queue<N> closed = new LinkedList<>();

	private final Map<N, N> parents = new SmallMap<>();
	private final Map<N, C> gmap = new SmallMap<>();
	private final Map<N, C> fmap = new SmallMap<>();

	protected abstract C getFScore(C value, C other);
	protected abstract C getGScore(N node, N other);
	protected abstract C getHScore(N node, N end);
	protected abstract Iterable<N> getAvailableNodes(N node);
	protected abstract boolean isEndNode(N node, N end);

	public final List<N> search(N start, N end)
	{
		try
		{
			//First node to be evaluated...
			opened.add(start);

			//Initialization...
			gmap.put(start, getGScore(start, start));
			fmap.put(start, getHScore(start, end));

			N node = null;
			while (!opened.isEmpty())
			{
				//Get the node with lowest f
				node = opened.peek();
				C lowest = fmap.get(node);
				for (N open : opened)
				{
					if (open == node) continue;

					C c = fmap.get(open);
					if (c.compareTo(lowest) < 0)
					{
						node = open;
						lowest = c;
					}
				}

				//If reached the end...
				if (isEndNode(node, end))
					break;

				opened.remove(node);
				closed.add(node);

				//For all neighboring nodes...
				for(N next : getAvailableNodes(node))
				{
					if (closed.contains(next)) continue;

					if (!opened.contains(next))
					{
						opened.add(next);
					}

					//Possible g score for next node
					C gnext = getFScore(gmap.get(node), getGScore(node, next));

					//if already in opened set, then check if it is shorter...
					C gcurr = gmap.get(next);
					if (gcurr != null)
					{
						if (gnext.compareTo(gcurr) <= 0) continue;
					}

					//Save the path...
					parents.put(next, node);
					gmap.put(next, gnext);
					fmap.put(next, getFScore(gnext, getHScore(next, end)));
				}
			}

			//Could not find a path...
			if (node == null || !isEndNode(node, end))
			{
				throw new IllegalArgumentException("cannot find path!");
			}

			//Convert parent pointers to a path...
			List<N> nodes = new ArrayList<>();
			while (!start.equals(node))
			{
				nodes.add(0, node);
				node = parents.get(node);
			}
			nodes.add(0, start);

			return nodes;
		}
		finally
		{
			this.reset();
		}
	}

	protected final void reset()
	{
		opened.clear();
		closed.clear();
		parents.clear();
		gmap.clear();
		fmap.clear();
	}
}
