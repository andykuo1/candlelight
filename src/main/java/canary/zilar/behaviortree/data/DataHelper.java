package canary.zilar.behaviortree.data;

import canary.zilar.behaviortree.Key;

public class DataHelper
{
	protected final BehaviorData data;

	public DataHelper(BehaviorData data)
	{
		this.data = data;
	}

	public NodeData global()
	{
		return this.data.getGlobalData();
	}

	public NodeData tree(String tree)
	{
		return this.data.getTreeData(tree);
	}

	public NodeData node(String tree, String node)
	{
		return this.data.getNodeData(tree, node);
	}

	public <E> NodeDataEntry<E> globalEntry(String key, E defaultValue)
	{
		return new NodeDataEntry<E>(this.global(), Key.GLOBAL(key), defaultValue);
	}

	public <E> NodeDataEntry<E> globalEntry(String key)
	{
		return new NodeDataEntry<E>(this.global(), Key.GLOBAL(key));
	}

	public <E> NodeDataEntry<E> treeEntry(String key, E defaultValue, String tree)
	{
		return new NodeDataEntry<E>(this.tree(tree), Key.TREE(key), defaultValue);
	}

	public <E> NodeDataEntry<E> treeEntry(String key, String tree)
	{
		return new NodeDataEntry<E>(this.tree(tree), Key.TREE(key));
	}

	public <E> NodeDataEntry<E> nodeEntry(String key, E defaultValue, String tree, String node)
	{
		return new NodeDataEntry<E>(this.node(tree, node), Key.NODE(key), defaultValue);
	}

	public <E> NodeDataEntry<E> nodeEntry(String key, String tree, String node)
	{
		return new NodeDataEntry<E>(this.node(tree, node), Key.NODE(key));
	}
}
