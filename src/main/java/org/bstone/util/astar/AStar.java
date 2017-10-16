package org.bstone.util.astar;

import org.bstone.util.small.SmallMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Created by Andy on 9/26/17.
 */
public class AStar<N, C extends Comparable<C>>
{
	protected final BiFunction<C, C, C> fscore;
	protected final BiFunction<N, N, C> gscore;
	protected final BiFunction<N, N, C> hscore;
	protected final Function<N, Iterable<N>> inext;

	protected final BiPredicate<N, N> isend;

	private final Queue<N> opened = new LinkedList<>();
	private final Queue<N> closed = new LinkedList<>();

	private final Map<N, N> parents = new SmallMap<>();
	private final Map<N, C> gmap = new SmallMap<>();
	private final Map<N, C> fmap = new SmallMap<>();

	public AStar(BiFunction<C, C, C> fscore,
	             BiFunction<N, N, C> gscore,
	             BiFunction<N, N, C> hscore,
	             Function<N, Iterable<N>> inext,
	             BiPredicate<N, N> isend)
	{
		this.fscore = fscore;
		this.gscore = gscore;
		this.hscore = hscore;
		this.inext = inext;
		this.isend = isend;
	}

	public final List<N> search(N start, N end)
	{
		try
		{
			//First node to be evaluated...
			opened.add(start);

			//Initialization...
			gmap.put(start, gscore.apply(start, start));
			fmap.put(start, hscore.apply(start, end));

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
				if (isend.test(node, end))
					break;

				opened.remove(node);
				closed.add(node);

				//For all neighboring nodes...
				for(N next : inext.apply(node))
				{
					if (closed.contains(next)) continue;

					if (!opened.contains(next))
					{
						opened.add(next);
					}

					//Possible g score for next node
					C gnext = fscore.apply(gmap.get(node), gscore.apply(node, next));

					//if already in opened set, then check if it is shorter...
					C gcurr = gmap.get(next);
					if (gcurr != null)
					{
						if (gnext.compareTo(gcurr) <= 0) continue;
					}

					//Save the path...
					parents.put(next, node);
					gmap.put(next, gnext);
					fmap.put(next, fscore.apply(gnext, hscore.apply(next, end)));
				}
			}

			//Could not find a path...
			if (node == null || !isend.test(node, end))
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
