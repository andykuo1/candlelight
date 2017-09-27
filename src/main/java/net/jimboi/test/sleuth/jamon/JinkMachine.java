package net.jimboi.test.sleuth.jamon;

/**
 * Created by Andy on 9/26/17.
 */
public class JinkMachine
{
	public int evaluate(Jamon user, Accion action)
	{
		return action.getCost(user);
	}

	public Accion evaluateAll(Jamon user, Iterable<Accion> actions)
	{
		Accion result = null;
		int cost = 0;
		for(Accion action : actions)
		{
			int i = this.evaluate(user, action);
			if (result == null || i < cost)
			{
				result = action;
				cost = i;
			}
		}
		return result;
	}
}
