package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Key;
import canary.zilar.behaviortree.Result;

public class TreeGetter extends Leaf
{
	public TreeGetter(String[] keys)
	{
		super(keys);
	}

	public TreeGetter(String tree, String treeKey, String key)
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
		Object value = cursor.getDataHelper().tree(this.tree).get(this.treeKey.toString());
		cursor.getDataHelper().tree().set(this.key.toString(), value);

		return Result.SUCCESS;
	}
}
