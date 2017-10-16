package net.jimboi.test.message;

/**
 * Created by Andy on 10/11/17.
 */
public final class Message
{
	private static final int MAX_ARGUMENT_LENGTH = 8;

	private String type = "generic";
	private Priority priority = Priority.LOW;

	private final String[] keys;
	private final Object[] values;

	public Message(String type)
	{
		this.type = type;

		this.keys = new String[MAX_ARGUMENT_LENGTH];
		this.values = new Object[MAX_ARGUMENT_LENGTH];
	}

	public final Message setType(String type)
	{
		this.type = type;
		return this;
	}

	public final Message setPriority(Priority priority)
	{
		this.priority = priority;
		return this;
	}

	public final Message set(String key, Object value)
	{
		for(int i = 0; i < this.keys.length; ++i)
		{
			if (this.keys[i] == null)
			{
				this.keys[i] = key;
				this.values[i] = value;
				return this;
			}
		}

		throw new IndexOutOfBoundsException("max argument length reached");
	}

	public final Object remove(String key)
	{
		for(int i = 0; i < this.keys.length; ++i)
		{
			if (key.equals(this.keys[i]))
			{
				this.keys[i] = null;
				return this.values[i];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public final <T> T get(String key)
	{
		for(int i = 0; i < this.keys.length; ++i)
		{
			if (key.equals(this.keys[i]))
			{
				return (T) this.values[i];
			}
		}

		throw new IllegalArgumentException("could not find key '" + key + "'");
	}

	public final boolean contains(String key)
	{
		for(int i = 0; i < this.keys.length; ++i)
		{
			if (key.equals(this.keys[i]))
			{
				return true;
			}
		}
		return false;
	}

	public final String getType()
	{
		return this.type;
	}

	public final Priority getPriority()
	{
		return this.priority;
	}

	public enum Priority
	{
		HIGH,
		MEDIUM,
		LOW;
	}
}
