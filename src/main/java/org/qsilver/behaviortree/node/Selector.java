package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class Selector extends Composite
{
	public Selector(String[] keys)
	{
		super(keys);
	}

	public Selector()
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
		for (int i = 0; i < this.size(); ++i)
		{
			Result r = this.get(i).execute(cursor);

			if (r != Result.FAILURE)
			{
				return r;
			}
		}

		return Result.FAILURE;
	}
}
