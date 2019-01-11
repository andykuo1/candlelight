package canary.test.gumshoe.test.opportunity.inspection.simulation.bti;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Andy on 12/14/17.
 */
public class BehaviorTree
{
	private final DeciderGroup root;
	private final Map<Decider, DeciderData> data;
	private final Stack<Decider> path;

	public BehaviorTree(DeciderGroup root)
	{
		this.root = root;
		this.data = new HashMap<>();
		this.path = new Stack<>();
	}

	public void tick()
	{
		this.tick(this.root, this.path);
	}

	protected void tick(DeciderGroup root, Stack<Decider> path)
	{
		BehaviorTraverser traverser = null;

		int level;
		boolean dirty = false;
		Stack<Iterator<Decider>> iters = new Stack<>();
		Iterator<Decider> iter = root.getIterator(traverser);
		iters.push(iter);

		if (path.isEmpty())
		{
			path.push(root);
			root.activate(traverser);
			System.out.println("A new path begins...");
		}

		level = 1;

		while(!iters.isEmpty())
		{
			boolean active = false;
			Decider decider = null;
			while (iter.hasNext() && !active)
			{
				System.out.println("Checking next child...");

				decider = iter.next();
				if (!dirty && level < path.size())
				{
					System.out.println("Comparing similar level..." + path.get(level) + " with " + decider);
					if (path.get(level) != decider)
					{
						if (decider.decide(traverser))
						{
							System.out.println("Found divergent active path...");

							while (path.size() > level)
							{
								Decider decider1 = path.pop();
								decider1.deactivate(traverser);
								System.out.println(decider1 + " is deactivated...");
							}

							System.out.println(decider.toString() + " is activated...");

							path.push(decider);
							decider.activate(traverser);
							dirty = true;
							active = true;
						}
					}
					else
					{
						active = true;
					}
				}
				else if (decider.decide(traverser))
				{
					System.out.println("Activate " + decider.toString());

					path.push(decider);
					decider.activate(traverser);
					dirty = true;
					active = true;
				}
			}

			if (active)
			{
				if (decider instanceof DeciderGroup)
				{
					DeciderGroup deciderGroup = (DeciderGroup) decider;
					iter = deciderGroup.getIterator(traverser);
					iters.push(iter);
					++level;

					System.out.println("Found next decider group...");
				}
				else if (decider instanceof DeciderBehavior)
				{
					System.out.println("Found behavior...");

					DeciderBehavior deciderBehavior = (DeciderBehavior) decider;

					if (!dirty)
					{
						if (deciderBehavior.tick())
						{
							System.out.println("Tick behavior...");
							return;
						}
						else
						{
							System.out.println("Failed behavior...");
							System.out.println("Found divergent active path...");
							while (path.size() > level)
							{
								Decider decider1 = path.pop();
								decider1.deactivate(traverser);
								System.out.println(decider1 + " is deactivated...");
							}
							dirty = true;
						}
					}
					else
					{
						System.out.println("Activated behavior...");
						return;
					}
				}
			}
			else
			{
				System.out.println("No accepted children, go back up...");
				iters.pop();
				if (iters.isEmpty()) return;
				iter = iters.peek();
			}
		}
	}

	public DeciderGroup getRoot()
	{
		return this.root;
	}

	private static final class DeciderData
	{
		private boolean enabled;
	}
}
