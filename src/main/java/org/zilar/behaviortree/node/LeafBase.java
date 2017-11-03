package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class LeafBase extends Leaf
{
	public LeafBase(String[] keys)
	{
		super(keys);
	}

	public LeafBase()
	{
		super(new String[0]);
	}

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		return Result.SUCCESS;
	}
}
