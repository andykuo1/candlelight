package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Key;
import org.qsilver.behaviortree.Result;

//TODO: Make sure to block sharing nodes between BehaviorData's
public class NodeSetter extends Leaf
{
	public NodeSetter(String[] keys)
	{
		super(keys);
	}

	public NodeSetter(String tree, String node, String nodeKey, String key)
	{
		this(new String[]{tree, node, nodeKey, key});
	}

	private String tree;
	private String node;
	private Key nodeKey;
	private Key key;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.tree = (String) cursor.getDataHelper().keyEntry(0).get();
		this.node = (String) cursor.getDataHelper().keyEntry(1).get();
		this.nodeKey = Key.TREE(cursor.getDataHelper().key(2));
		this.key = Key.TREE(cursor.getDataHelper().key(3));

		return this.tree != null && this.node != null;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		Object value = cursor.getDataHelper().tree().get(this.key.toString());
		cursor.getDataHelper().node(this.tree, this.node).set(this.nodeKey.toString(), value);

		return Result.SUCCESS;
	}
}
