package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

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
