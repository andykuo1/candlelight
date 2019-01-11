package canary.zilar.behaviortree.data;

import java.util.HashMap;
import java.util.Map;

public final class NodeDataPool<E extends NodeData> extends NodeData
{
	private Map<String, E> nodes = new HashMap<String, E>();

	protected NodeDataPool(String id)
	{
		super(id);
	}

	public final E setNode(E node)
	{
		node.setSuperNode(this);
		return this.nodes.put(node.getID(), node);
	}

	public final E getNode(String id)
	{
		return this.nodes.get(id);
	}
}
