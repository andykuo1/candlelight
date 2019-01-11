package canary.zilar.behaviortree.data;

import canary.zilar.behaviortree.Key;
import canary.zilar.behaviortree.node.GlobalGetter;
import canary.zilar.behaviortree.node.Node;
import canary.zilar.behaviortree.node.NodeGetter;
import canary.zilar.behaviortree.node.TreeGetter;

/**
 * A wrapper to access an entry in a DataNode with key;
 * <br> Precondition: isType(Class<E>) returns true
 */
@SuppressWarnings("unchecked")
public final class NodeDataEntry<E extends Object>
{
	protected final NodeData data;
	protected final Key key;

	protected NodeDataEntry(NodeData data, Key key, E defaultValue)
	{
		this.data = data;
		this.key = key;
		this.validate(defaultValue);
		if (!this.exists())
		{
			this.set(defaultValue);
		}
	}

	protected NodeDataEntry(NodeData data, Key key)
	{
		this.data = data;
		this.key = key;
	}

	public final boolean isValidFor(E value)
	{
		if (value == null)
		{
			throw new IllegalArgumentException();
		}

		return value.getClass().isInstance(this.get());
	}

	public final NodeDataEntry<E> validate(E defaultValue)
	{
		if (!this.isValidFor(defaultValue))
		{
			this.data.set(this.key.toString(), defaultValue);
		}

		return this;
	}

	public final E set(E value)
	{
		return (E) this.data.set(this.key.toString(), value);
	}

	public final E remove()
	{
		return (E) this.data.remove(this.key.toString());
	}

	public final E get(E defaultValue)
	{
		return this.data.get(this.key.toString(), defaultValue);
	}

	public final E get()
	{
		return (E) this.data.get(this.key.toString());
	}

	public final boolean exists()
	{
		return this.data.contains(this.key.toString());
	}

	public final boolean isNull()
	{
		return this.data.get(this.key.toString()) == null;
	}

	//TODO: Problem with this is that it is dependent on BehaviorData!
	public final Node createGetterNode(String key)
	{
		switch (this.key.getScope())
		{
			case GLOBAL:
				return new GlobalGetter(this.key.toString(), key);
			case TREE:
				return new TreeGetter(this.data.getID(), this.key.toString(), key);
			case NODE:
				return new NodeGetter(this.data.getSuperNode().getID(), this.data.getID(), this.key.toString(), key);
		}

		return null;
	}
}
