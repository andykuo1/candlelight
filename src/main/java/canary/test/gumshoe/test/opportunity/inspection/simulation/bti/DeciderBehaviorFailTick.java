package canary.test.gumshoe.test.opportunity.inspection.simulation.bti;

/**
 * Created by Andy on 12/14/17.
 */
public class DeciderBehaviorFailTick extends DeciderBehavior
{
	public DeciderBehaviorFailTick(String name)
	{
		super(name);
	}

	@Override
	public boolean tick()
	{
		return false;
	}
}
