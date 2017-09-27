package net.jimboi.test.sleuth.comm.environ;

/**
 * Created by Andy on 9/27/17.
 */
public class GoalState extends Environment
{
	private final Iterable<Goal> goals;

	public GoalState(Iterable<Goal> goals)
	{
		this.goals = goals;
	}

	public boolean isSatisfied(Environment env)
	{
		for(Goal goal : this.goals)
		{
			if (goal.isSatisfied(env)) return true;
		}

		return false;
	}

	public int getRemainingCostValue(Environment env)
	{
		int work = 0;
		for(Goal goal : this.goals)
		{
			work += goal.getRemainingWork(env);
		}
		return work;
	}

	@Override
	public FiniteEnvironment setup(FiniteEnvironment dst)
	{
		for(Goal goal : this.goals)
		{
			goal.setSatisfied(dst);
		}
		return dst;
	}

	@Override
	public boolean contains(String state, boolean enabled)
	{
		for(Goal goal : this.goals)
		{
			if (goal.contains(state, enabled))
			{
				return true;
			}
		}
		return false;
	}
}
