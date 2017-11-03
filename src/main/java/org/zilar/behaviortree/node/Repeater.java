package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class Repeater extends Decorator
{
	public Repeater(String[] keys)
	{
		super(keys);
	}

	public Repeater(String iterations)
	{
		this(new String[]{iterations});
	}

	private int iterations;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.iterations = cursor.getDataHelper().keyEntry(0, 0).get();

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		for (int i = 0; i < this.iterations; ++i)
		{
			Result r = this.get().execute(cursor);
			if (r != Result.SUCCESS && r != Result.FAILURE)
			{
				return r;
			}
		}

		return Result.SUCCESS;
	}
}
