package org.zilar.behaviortree.node;

import org.zilar.behaviortree.Cursor;
import org.zilar.behaviortree.Result;

import java.lang.reflect.InvocationTargetException;

public abstract class Node
{
	public static String createUUID()
	{
		char[] s = new char[36];
		String hex = "0123456789abcdef";
		for (int i = 0; i < 36; ++i)
		{
			s[i] = hex.charAt((int) Math.floor(Math.random() * 0x10));
		}

		s[14] = '4';
		s[19] = hex.charAt((s[19] & 0x3) | 0x8);
		s[8] = s[13] = s[18] = s[23] = '-';

		return String.valueOf(s);
	}

	public final String id = createUUID();
	private final String[] keys;

	public Node(String[] keys)
	{
		this.keys = keys;

		try
		{
			this.getClass().getConstructor(this.keys.getClass());
		}
		catch (NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public final Result execute(Cursor cursor)
	{
		this.enter(cursor);

		if (!this.isOpen(cursor))
		{
			if (!this.open(cursor))
			{
				this.close(cursor);
				this.exit(cursor);
				return Result.ERROR;
			}
		}

		Result r = this.tick(cursor);

		if (r != Result.RUNNING)
		{
			this.close(cursor);
		}

		this.exit(cursor);
		return r;
	}

	public final boolean open(Cursor cursor)
	{
		cursor.getBehaviorData().getData(cursor.getBehaviorTree().id, this.id).set("_open", true);
		cursor.onNodeOpen(this);
		return this.onOpen(cursor);
	}

	public final void close(Cursor cursor)
	{
		cursor.getBehaviorData().getData(cursor.getBehaviorTree().id, this.id).set("_open", false);
		cursor.onNodeClose(this);
		this.onClose(cursor);
	}

	public final void enter(Cursor cursor)
	{
		cursor.onNodeEnter(this);
		this.onEnter(cursor);
	}

	public final void exit(Cursor cursor)
	{
		cursor.onNodeExit(this);
		this.onExit(cursor);
	}

	public final Result tick(Cursor cursor)
	{
		cursor.onNodeTick(this);

		return this.onTick(cursor);
	}

	public Node copy()
	{
		String[] keys = new String[this.keys.length];
		for (int i = 0; i < keys.length; ++i)
		{
			keys[i] = this.keys[i];
		}

		Node node = null;
		try
		{
			node = this.getClass().getDeclaredConstructor(keys.getClass()).newInstance((Object) keys);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			return null;
		}

		return node;
	}

	public String getKey(int keyIndex)
	{
		if (keyIndex < 0 || keyIndex >= this.keys.length)
		{
			throw new IllegalArgumentException();
		}

		return this.keys[keyIndex];
	}

	public final boolean isOpen(Cursor cursor)
	{
		return cursor.getBehaviorData().getData(cursor.getBehaviorTree().id, this.id).get("_open", false);
	}

	protected abstract boolean onOpen(Cursor cursor);

	protected void onClose(Cursor cursor)
	{
	}

	protected void onEnter(Cursor cursor)
	{
	}

	protected void onExit(Cursor cursor)
	{
	}

	protected abstract Result onTick(Cursor cursor);
}
