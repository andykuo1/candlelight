package org.zilar.behaviortree.node.test;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;
import org.zilar.behaviortree.node.Leaf;

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
