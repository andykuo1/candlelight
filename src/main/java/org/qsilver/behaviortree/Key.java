package org.qsilver.behaviortree;

//TODO: Implement this for the behavior tree instead of simply Strings, since that gets confusing
public final class Key
{
	public static Key GLOBAL(final String key)
	{
		return new Key(Scope.GLOBAL, key);
	}

	public static Key TREE(final String key)
	{
		return new Key(Scope.TREE, key);
	}

	public static Key NODE(final String key)
	{
		return new Key(Scope.NODE, key);
	}

	public enum Scope
	{
		GLOBAL,
		TREE,
		NODE
	}

	private final Scope scope;
	private final String key;

	private Key(final Scope scope, final String key)
	{
		this.scope = scope;
		this.key = key;
	}

	public final Scope getScope()
	{
		return this.scope;
	}

	@Override
	public String toString()
	{
		return this.key;
	}
}
