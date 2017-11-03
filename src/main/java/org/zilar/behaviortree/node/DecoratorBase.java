package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

public class DecoratorBase extends Decorator
{
	public DecoratorBase(String[] keys)
	{
		super(keys);
	}

	public DecoratorBase()
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
		return this.get().execute(cursor);
	}
}
