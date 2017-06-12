package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Key;
import org.qsilver.behaviortree.Result;

public class GlobalGetter extends Leaf
{
	public GlobalGetter(String[] keys)
	{
		super(keys);
	}

	public GlobalGetter(String globalKey, String key)
	{
		this(new String[]{globalKey, key});
	}

	private Key globalKey;
	private Key key;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.globalKey = Key.GLOBAL(cursor.getDataHelper().key(0));
		this.key = Key.TREE(cursor.getDataHelper().key(1));

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		Object value = cursor.getDataHelper().global().get(this.globalKey.toString());
		cursor.getDataHelper().tree().set(this.key.toString(), value);

		return Result.SUCCESS;
	}
}
