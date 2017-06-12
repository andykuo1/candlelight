package org.qsilver.behaviortree.node;

import java.util.ArrayList;
import java.util.List;

/** A node that contains children */
public abstract class Composite extends Node
{
	private List<Node> nodes = new ArrayList<Node>();

	public Composite(String[] keys)
	{
		super(keys);
	}

	public Composite add(Node node)
	{
		this.nodes.add(node);
		return this;
	}

	public Node get(int i)
	{
		return this.nodes.get(i);
	}

	public int size()
	{
		return this.nodes.size();
	}

	@Override
	public Composite copy()
	{
		Composite comp = (Composite) super.copy();
		for (int i = 0; i < this.size(); ++i)
		{
			comp.add(this.get(i).copy());
		}
		return comp;
	}
}
