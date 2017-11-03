package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class Sequence extends Composite
{
	public Sequence(String[] keys)
	{
		super(keys);
	}

	public Sequence()
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

			if (r != Result.SUCCESS)
			{
				return r;
			}
		}

		return Result.SUCCESS;
	}
}
