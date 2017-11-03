package org.zilar.behaviortree.node;

/** A node that contains ONE child */
public abstract class Decorator extends Node
{
	private Node node;

	public Decorator(String[] keys)
	{
		super(keys);
	}

	public Decorator set(Node node)
	{
		this.node = node;
		return this;
	}

	public Node get()
	{
		return this.node;
	}

	@Override
	public Decorator copy()
	{
		Decorator deco = (Decorator) super.copy();
		deco.set(this.get().copy());
		return deco;
	}
}
