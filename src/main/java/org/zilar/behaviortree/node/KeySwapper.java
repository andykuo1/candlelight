package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class KeySwapper extends Leaf
{
	public KeySwapper(String[] keys)
	{
		super(keys);
	}

	public KeySwapper(String oldKey, String newKey)
	{
		this(new String[]{oldKey, newKey});
	}

	private String oldKey;
	private String newKey;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.oldKey = cursor.getDataHelper().key(0);
		this.newKey = cursor.getDataHelper().key(1);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		Object oldValue = cursor.getDataHelper().tree().get(this.oldKey);
		Object newValue = cursor.getDataHelper().tree().get(this.newKey);

		cursor.getDataHelper().tree().set(this.oldKey, newValue);
		cursor.getDataHelper().tree().set(this.newKey, oldValue);

		return Result.SUCCESS;
	}
}
