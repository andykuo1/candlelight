package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class Inverter extends Decorator
{
	public Inverter(String[] keys)
	{
		super(keys);
	}

	public Inverter()
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
		Result r = this.get().execute(cursor);

		if (r == Result.SUCCESS)
		{
			r = Result.FAILURE;
		}
		else if (r == Result.FAILURE)
		{
			r = Result.SUCCESS;
		}

		return r;
	}
}
