package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class MemSequence extends Composite
{
	public MemSequence(String[] keys)
	{
		super(keys);
	}

	public MemSequence()
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

			if (r != Result.SUCCESS)
			{
				if (r == Result.RUNNING)
				{
					cursor.getDataHelper().node().set("node_index", i);
				}
				return r;
			}
		}

		return Result.SUCCESS;
	}

}
