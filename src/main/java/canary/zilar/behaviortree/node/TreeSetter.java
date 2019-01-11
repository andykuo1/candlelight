package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Key;
import canary.zilar.behaviortree.Result;

public class TreeSetter extends Leaf
{
	public TreeSetter(String[] keys)
	{
		super(keys);
	}

	public TreeSetter(String tree, String treeKey, String key)
	{
		this(new String[]{tree, treeKey, key});
	}

	private String tree;
	private Key treeKey;
	private Key key;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.tree = (String) cursor.getDataHelper().keyEntry(0).get();
		this.treeKey = Key.TREE(cursor.getDataHelper().key(1));
		this.key = Key.TREE(cursor.getDataHelper().key(2));

		return this.tree != null;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		Object value = cursor.getDataHelper().tree().get(this.key.toString());
		cursor.getDataHelper().tree(this.tree).set(this.treeKey.toString(), value);

		return Result.SUCCESS;
	}
}
