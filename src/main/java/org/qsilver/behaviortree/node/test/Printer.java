package org.qsilver.behaviortree.node.test;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;
import org.qsilver.behaviortree.node.Leaf;

public class Printer extends Leaf
{
	public Printer(String[] keys)
	{
		super(keys);
	}

	public Printer(String string)
	{
		this(new String[]{string});
	}

	private String string;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.string = cursor.getDataHelper().key(0);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		System.out.println(this.string);

		return Result.SUCCESS;
	}
}
