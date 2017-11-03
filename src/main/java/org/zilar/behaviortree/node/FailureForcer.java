package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class FailureForcer extends Decorator
{
	public FailureForcer(String[] keys)
	{
		super(keys);
	}

	public FailureForcer()
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

		return Result.FAILURE;
	}
}
