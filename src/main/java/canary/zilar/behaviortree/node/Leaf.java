package canary.zilar.behaviortree.node;

/** A node that contains NO children */
public abstract class Leaf extends Node
{
	public Leaf(String[] keys)
	{
		super(keys);
	}
}