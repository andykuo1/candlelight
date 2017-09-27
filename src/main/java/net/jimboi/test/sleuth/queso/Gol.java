package net.jimboi.test.sleuth.queso;

/**
 * Created by Andy on 9/26/17.
 */
public abstract class Gol
{
	public abstract String[] getConditions();

	public boolean hasConditions(EstadoMachina states)
	{
		for(String state : this.getConditions())
		{
			if (!states.contains(state))
			{
				return false;
			}
		}

		return true;
	}
}
