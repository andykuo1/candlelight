package org.qsilver.behaviortree.node;

import org.qsilver.behaviortree.Cursor;
import org.qsilver.behaviortree.Result;

import java.util.Stack;

public class StackPusher extends Leaf
{
	public StackPusher(String[] keys)
	{
		super(keys);
	}

	public StackPusher(String stack, String key)
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
		if (!cursor.getDataHelper().tree().contains(this.key))
		{
			return Result.FAILURE;
		}

		Object value = cursor.getDataHelper().tree().get(this.key);

		this.stack.push(value);

		return Result.SUCCESS;
	}
}
