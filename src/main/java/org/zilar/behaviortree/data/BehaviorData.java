package org.zilar.behaviortree.data;

import org.zilar.behaviortree.Key;

import java.util.Stack;

/**
 * Serves as a pool of cached data for the node tree;
 * <br>A single run should always contain the same instance
 */
public final class BehaviorData
{
	private final NodeDataPool<NodeDataPool<NodeData>> data = new NodeDataPool<NodeDataPool<NodeData>>("global");
	private final DataHelper dataHelper;

	public BehaviorData()
	{
		this.dataHelper = new DataHelper(this);
	}

	public Object set(String key, Object value)
	{
		return this.data.set(key, value);
	}

	public Object get(String key)
	{
		return this.data.get(key);
	}

	public <E> NodeDataEntry<E> entry(String key, E defaultValue)
	{
		return new NodeDataEntry<E>(this.data, Key.GLOBAL(key), defaultValue);
	}

	public <E> NodeDataEntry<E> entry(String key)
	{
		return new NodeDataEntry<E>(this.data, Key.GLOBAL(key));
	}

	public NodeDataPool<NodeDataPool<NodeData>> getGlobalData()
	{
		return this.data;
	}

	public NodeDataPool<NodeData> getTreeData(String tree)
	{
		NodeDataPool<NodeData> data = this.getGlobalData().getNode(tree);
		if (data == null)
		{
			data = new NodeDataPool<NodeData>(tree);
			data.set("_open_nodes", new Stack<String>());
			data.set("_return", null);
			this.data.setNode(data);
		}
		return data;
	}

	public NodeData getNodeData(String tree, String node)
	{
		NodeData data = this.getTreeData(tree).getNode(node);
		if (data == null)
		{
			data = new NodeData(node);
			this.data.getNode(tree).setNode(data);
		}
		return data;
	}

	public NodeData getData(String tree, String node)
	{
		if (tree != null)
		{
			if (node != null)
			{
				return this.getNodeData(tree, node);
			}
			else
			{
				return this.getTreeData(tree);
			}
		}
		else
		{
			return this.getGlobalData();
		}
	}

	public DataHelper getDataHelper()
	{
		return this.dataHelper;
	}
}
