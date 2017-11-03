package org.zilar.behaviortree.data;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Key;

public class LocalDataHelper extends DataHelper
{
	protected final Cursor cursor;

	public LocalDataHelper(Cursor cursor)
	{
		super(cursor.getBehaviorData());

		this.cursor = cursor;
	}

	public NodeData tree()
	{
		return this.data.getTreeData(this.cursor.getBehaviorTree().id);
	}

	public NodeData node(String id)
	{
		return this.data.getNodeData(this.cursor.getBehaviorTree().id, id);
	}

	public NodeData node()
	{
		return this.data.getNodeData(this.cursor.getBehaviorTree().id, this.cursor.getCurrentNode().id);
	}

	public String key(int keyIndex)
	{
		return this.cursor.getCurrentNode().getKey(keyIndex);
	}

	public <E> NodeDataEntry<E> treeEntry(String key, E defaultValue)
	{
		return new NodeDataEntry<E>(this.tree(), Key.TREE(key), defaultValue);
	}

	public <E> NodeDataEntry<E> treeEntry(String key)
	{
		return new NodeDataEntry<E>(this.tree(), Key.TREE(key));
	}

	public <E> NodeDataEntry<E> nodeEntry(String key, E defaultValue)
	{
		return new NodeDataEntry<E>(this.node(), Key.NODE(key), defaultValue);
	}

	public <E> NodeDataEntry<E> nodeEntry(String key)
	{
		return new NodeDataEntry<E>(this.node(), Key.NODE(key));
	}

	public <E> NodeDataEntry<E> keyEntry(int keyIndex, E defaultValue)
	{
		return this.treeEntry(this.cursor.getCurrentNode().getKey(keyIndex), defaultValue);
	}

	public <E> NodeDataEntry<E> keyEntry(int keyIndex)
	{
		return this.treeEntry(this.cursor.getCurrentNode().getKey(keyIndex));
	}
}
