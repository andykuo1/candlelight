package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

public class MemSelector extends Composite
{
	public MemSelector(String[] keys)
	{
		super(keys);
	}

	public MemSelector()
	{
		this(new String[0]);
	}

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		cursor.getDataHelper().node().set("node_index", 0);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		int node = cursor.getDataHelper().node().get("node_index", 0);

		for (int i = node; i < this.size(); ++i)
		{
			Result r = this.get(i).execute(cursor);

			if (r != Result.FAILURE)
			{
				if (r == Result.RUNNING)
				{
					cursor.getDataHelper().node().set("node_index", i);
				}
				return r;
			}
		}

		return Result.FAILURE;
	}
}
