package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class FailureForcer extends Decorator
{
	public FailureForcer(String[] keys)
	{
		super(keys);
	}

	public FailureForcer()
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
		this.get().execute(cursor);

		return Result.FAILURE;
	}
}
