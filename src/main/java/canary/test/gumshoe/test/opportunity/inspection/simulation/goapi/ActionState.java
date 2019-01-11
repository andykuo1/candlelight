package canary.test.gumshoe.test.opportunity.inspection.simulation.goapi;

/**
 * Created by Andy on 12/9/17.
 */
public class ActionState implements Condition, Effect
{
	private final String id;
	private final boolean value;

	public ActionState(String id, boolean value)
	{
		this.id = id;
		this.value = value;
	}

	@Override
	public boolean apply(Environment env)
	{
		return env.set(this.id, this.value);
	}

	@Override
	public boolean test(Environment env)
	{
		Boolean b = env.get(this.id);
		return b != null && b == this.value;
	}
}
