package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class SetString extends Leaf
{
	public SetString(String[] keys)
	{
		super(keys);
	}

	public SetString(String key, String value)
	{
		this(new String[]{key, value});
	}

	private String key;
	private String value;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.key = cursor.getDataHelper().keyEntry(0, "").get();
		this.value = cursor.getDataHelper().key(1);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		cursor.getDataHelper().tree().set(this.key, this.value);
		return Result.SUCCESS;
	}
}
