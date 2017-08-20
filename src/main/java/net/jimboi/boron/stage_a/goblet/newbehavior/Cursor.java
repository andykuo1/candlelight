package net.jimboi.boron.stage_a.goblet.newbehavior;

import java.util.Stack;

/**
 * Created by Andy on 8/12/17.
 */
public class Cursor
{
	public Blackboard data;
	public BehaviorTree tree;
	public Stack<Node> openNodes;
	public int nodeCount;
	public Object target;

	public void enter(Node node)
	{
		this.openNodes.push(node);
		++this.nodeCount;
	}

	public void open(Node node)
	{

	}

	public void tick(Node node)
	{

	}

	public void close(Node node)
	{

	}

	public void exit(Node node)
	{
		this.openNodes.pop();
	}
}
