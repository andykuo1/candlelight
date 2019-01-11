package canary.test.gumshoe.test.opportunity.inspection.simulation.goapi;

/**
 * Created by Andy on 12/9/17.
 */
public class SnapshotEnvironment extends Environment
{
	protected final Environment parent;

	protected Action action;

	public SnapshotEnvironment(Environment env)
	{
		this.parent = env;
	}

	public boolean process(Action action)
	{
		this.set(this.parent);
		this.action = null;

		if (action.applyEffects(this))
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
