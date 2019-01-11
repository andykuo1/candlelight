package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class LeafBase extends Leaf
{
	public LeafBase(String[] keys)
	{
		super(keys);
	}

	public LeafBase()
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
		return Result.SUCCESS;
	}
}
