package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Andy on 12/18/17.
 */
public class BehaviorTraverser
{
	private Map<Decider, Integer> mapping = new HashMap<>();

	private Map<BehaviorTree, List<Object>> treeData = new HashMap<>();
	private Map<Decider, List<Object>> nodeData = new HashMap<>();

	public BehaviorTraverser(BehaviorTree tree)
	{
		this.tree = tree;
		this.root = this.tree.getRoot();

		int i = 0;
		Stack<Iterator<Decider>> iters = new Stack<>();
		iters.push(this.tree.getRoot().getIterator(this));

		while(!iters.isEmpty())
		{
			Iterator<Decider> iter = iters.pop();
			while (iter.hasNext())
			{
				Decider decider = iter.next();
				this.mapping.put(decider, i);

				if (decider instanceof DeciderGroup)
				{
					iters.push(((DeciderGroup) decider).getIterator(this));
				}
			}
		}
	}

	private int level;
	private boolean dirty;
	private Stack<Iterator<Decider>> iterators = new Stack<>();
	private Stack<Decider> path = new Stack<>();
	private DeciderGroup root;
	private BehaviorTree tree;

	protected void tick()
	{
		int level = 0;
		this.dirty = false;

		Iterator<Decider> iter = root.getIterator(this);
		this.iterators.push(iter);

		if (path.isEmpty())
		{
			path.push(root);
			root.activate(this);
			System.out.println("A new path begins...");
		}

		level = 1;

		while(!this.iterators.isEmpty())
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
					if (path.get(level) == decider)
					{
						active = true;
					}
					else if (decider.decide(this))
					{
						this.resetPathTail(level);
						this.appendPath(decider);
						active = true;
					}
				}
				else if (decider.decide(this))
				{
					this.appendPath(decider);
					active = true;
				}
			}

			if (active)
			{
				if (decider instanceof DeciderGroup)
				{
					DeciderGroup deciderGroup = (DeciderGroup) decider;
					iter = deciderGroup.getIterator(this);
					this.iterators.push(iter);
					++level;

					System.out.println("Found next decider group...");
				}
				else if (decider instanceof DeciderBehavior)
				{
					System.out.println("Found behavior...");

					DeciderBehavior deciderBehavior = (DeciderBehavior) decider;

					if (dirty || deciderBehavior.tick())
					{
						System.out.println("Activate or Tick behavior...");
						break;
					}
					else
					{
						System.out.println("Failed behavior...");
						this.resetPathTail(this.level);
					}
				}
			}
			else
			{
				System.out.println("No accepted children, go back up...");
				this.iterators.pop();
				if (this.iterators.isEmpty()) break;
				iter = this.iterators.peek();
			}
		}

		this.iterators.clear();
	}

	public List<Object> getTreeData(BehaviorTree tree)
	{
		return this.treeData.computeIfAbsent(tree, behaviorTree -> new ArrayList<>());
	}

	public List<Object> getNodeData(Decider node)
	{
		return this.nodeData.computeIfAbsent(node, decider -> new ArrayList<>());
	}

	private void appendPath(Decider decider)
	{
		System.out.println("Activate " + decider.toString());
		this.path.push(decider);
		decider.activate(this);
		this.dirty = true;
	}

	private void resetPathTail(int tailIndex)
	{
		System.out.println("Found divergent active path...");
		while (this.path.size() > tailIndex)
		{
			Decider decider1 = this.path.pop();
			decider1.deactivate(this);
			System.out.println(decider1 + " is deactivated...");
		}
		this.dirty = true;
	}

	/*

	Deciders
		Decide
	BehaviorDeciders extends Deciders
		Activate
	GroupDeciders extends Deciders
		Deactivate

	Behavior
		Activate
		Deactivate
		Tick

	Group Policy
		Prioritized List
			- Can be interrupted by previous children
		Sequential
			- Will skip any not used
		Sequential Looping
		Probabilistic



	*/
}
