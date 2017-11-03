package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Key;
import org.zilar.behaviortree.Result;

public class GlobalSetter extends Leaf
{
	public GlobalSetter(String[] keys)
	{
		super(keys);
	}

	public GlobalSetter(String globalKey, String key)
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
		Object value = cursor.getDataHelper().tree().get(this.key.toString());
		cursor.getDataHelper().global().set(this.globalKey.toString(), value);

		return Result.SUCCESS;
	}
}
