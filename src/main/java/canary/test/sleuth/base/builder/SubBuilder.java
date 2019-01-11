package canary.test.sleuth.base.builder;

/**
 * Created by Andy on 10/7/17.
 */
public abstract class SubBuilder<E extends Builder>
{
	protected final E parent;

	public SubBuilder(E parent)
	{
		this.parent = parent;
	}

	public E pack()
	{
		return this.parent;
	}
}
