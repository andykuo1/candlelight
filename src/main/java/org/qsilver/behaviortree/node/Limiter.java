package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class Limiter extends Decorator
{
	public Limiter(String[] keys)
	{
		super(keys);
	}

	public Limiter(String maxIterations)
	{
		this(new String[]{maxIterations});
	}

	private int maxIterations;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.maxIterations = cursor.getDataHelper().keyEntry(0, 0).get();
		cursor.getDataHelper().node().set("iteration", 0);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		int iteration = cursor.getDataHelper().node().get("iteration", 0);

		if (iteration < this.maxIterations)
		{
			cursor.getDataHelper().node().set("iteration", ++iteration);
			return this.get().execute(cursor);
		}

		return Result.FAILURE;
	}
}
