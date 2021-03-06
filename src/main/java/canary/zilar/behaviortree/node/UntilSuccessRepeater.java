package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class UntilSuccessRepeater extends Decorator
{
	public UntilSuccessRepeater(String[] keys)
	{
		super(keys);
	}

	public UntilSuccessRepeater()
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
		while (r == Result.FAILURE);

		return Result.SUCCESS;
	}
}
