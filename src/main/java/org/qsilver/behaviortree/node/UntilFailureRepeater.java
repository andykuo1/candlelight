package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class UntilFailureRepeater extends Decorator
{
	public UntilFailureRepeater(String[] keys)
	{
		super(keys);
	}

	public UntilFailureRepeater()
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
		Result r;

		do
		{
			r = this.get().execute(cursor);
			if (r != Result.SUCCESS && r != Result.FAILURE)
			{
				return r;
			}
		}
		while (r == Result.SUCCESS);

		return Result.SUCCESS;
	}
}
