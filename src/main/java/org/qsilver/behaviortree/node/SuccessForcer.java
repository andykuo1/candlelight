package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class SuccessForcer extends Decorator
{
	public SuccessForcer(String[] keys)
	{
		super(keys);
	}

	public SuccessForcer()
	{
		this(new String[0]);
	}

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		this.get().execute(cursor);

		return Result.SUCCESS;
	}
}
