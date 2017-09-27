package net.jimboi.test.sleuth.comm.environ;

/**
 * Created by Andy on 9/27/17.
 */
public class ActionState extends FiniteEnvironment
{
	protected final Action action;

	public ActionState(Environment env, Action action)
	{
		if (env != null) env.setup(this);

		this.action = action;

		if (this.action != null)
		{
			this.action.predict(this);
		}
	}

	public Action getAction()
	{
		return this.action;
	}
}
