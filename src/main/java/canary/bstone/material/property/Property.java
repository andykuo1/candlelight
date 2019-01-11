package canary.bstone.material.property;

import canary.bstone.mogli.Program;

/**
 * Created by Andy on 10/31/17.
 */
public abstract class Property<T>
{
	private final String name;
	private final Class<T> type;

	public Property(Class<T> type, String name)
	{
		this.type = type;
		this.name = name;
	}

	public abstract void apply(Program program, T value);

	public boolean isSupportedValue(Object value)
	{
		return this.type.isInstance(value);
	}

	public final String getName()
	{
		return this.name;
	}

	public final Class<T> getType()
	{
		return this.type;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Property && this.name.equals(((Property) obj).name) && this.type.equals(((Property) obj).type);
	}
}
