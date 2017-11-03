package org.zilar.behaviortree.node.test;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;
import org.zilar.behaviortree.node.Leaf;

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
