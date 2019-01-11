package canary.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/27/17.
 */
public class ActionSnapshot extends MutableEnvironment
{
	protected final Environment parent;

	protected Action action;

	public ActionSnapshot(Environment env)
	{
		this.parent = env;
	}

	public boolean process(Action action)
	{
		this.set(this.parent);
		this.action = null;

		if (action.applyEffects(this, null))
		{
			this.action = action;
			return true;
		}

		return false;
	}

	public Action getAction()
	{
		return this.action;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Environment && this.action == null)
		{
			return this.parent.equals(o);
		}

		return super.equals(o);
	}
}
