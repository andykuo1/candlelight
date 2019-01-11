package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class SetInteger extends Leaf
{
	public SetInteger(String[] keys)
	{
		super(keys);
	}

	public SetInteger(String key, int value)
	{
		this(new String[]{key, "" + value});
	}

	private String key;
	private int value;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.key = cursor.getDataHelper().keyEntry(0, "").get();
		this.value = Integer.parseInt(cursor.getDataHelper().key(1));

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		cursor.getDataHelper().tree().set(this.key, this.value);
		return Result.SUCCESS;
	}
}
