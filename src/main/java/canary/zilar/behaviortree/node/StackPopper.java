package canary.zilar.behaviortree.node;

import canary.zilar.behaviortree.Cursor;
import canary.zilar.behaviortree.Result;

import java.util.Stack;

public class StackPopper extends Leaf
{
	public StackPopper(String[] keys)
	{
		super(keys);
	}

	public StackPopper(String stack, String key)
	{
		this(new String[]{stack, key});
	}

	private Stack<Object> stack;
	private String key;

	@Override
	protected boolean onOpen(Cursor cursor)
	{
		this.stack = cursor.getDataHelper().keyEntry(0, new Stack<Object>()).get();
		this.key = cursor.getDataHelper().key(1);

		return this.stack != null;
	}

	@Override
	protected Result onTick(Cursor cursor)
	{
		if (this.stack.isEmpty()) return Result.FAILURE;

		Object value = this.stack.pop();
		cursor.getDataHelper().tree().set(this.key, value);

		return Result.SUCCESS;
	}
}
