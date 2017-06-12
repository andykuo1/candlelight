package org.qsilver.behaviortree.node.test;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;
import org.qsilver.behaviortree.node.Leaf;

public class MemPrinter extends Leaf
{
	public MemPrinter(String[] keys)
	{
		super(keys);
	}

	public MemPrinter(String key)
	{
		this(new String[]{key});
	}

	private String key;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.key = cursor.getDataHelper().key(0);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		Object value = cursor.getDataHelper().tree().get(this.key);

		System.out.println(value == null ? "null" : value.toString());

		return Result.SUCCESS;
	}
}
