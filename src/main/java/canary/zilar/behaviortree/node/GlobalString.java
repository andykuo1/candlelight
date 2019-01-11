package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

public class GlobalString extends Leaf
{
	public GlobalString(String[] keys)
	{
		super(keys);
	}

	public GlobalString(String key, String value)
	{
		this(new String[]{key, value});
	}

	private String key;
	private String value;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.key = cursor.getDataHelper().keyEntry(0, "").get();
		this.value = cursor.getDataHelper().key(1);

		return true;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		cursor.getDataHelper().global().set(this.key, this.value);
		return Result.SUCCESS;
	}
}
