package net.jimboi.test.sleuth.queso;

/**
 * Created by Andy on 9/26/17.
 */
public abstract class Accion
{
	public static int NEXT_AVAILABLE_ID = 0;

	protected final int id;

	public Accion()
	{
		this.id = NEXT_AVAILABLE_ID++;
	}

	//These are called once every evaluation and can change accordingly
	public abstract String[] getRequiredConditions();
	public abstract String[] getResultedConditions();
	public abstract int getCost();

	public void apply(EstadoMachina states)
	{
		for(String state : this.getResultedConditions())
		{
			states.add(this.id, state);
		}
	}

	public boolean hasRequiredConditions(EstadoMachina states)
	{
		for(String state : this.getRequiredConditions())
		{
			if (!states.contains(state))
			{
				return false;
			}
		}

		return true;
	}

	public int getID()
	{
		return this.id;
	}
}
