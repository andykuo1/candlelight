package canary.test.pye;

/**
 * Created by Andy on 10/16/17.
 */
public class Appendage
{
	private final String name;
	private final int id;

	private final AppendageAction action;

	public Appendage(String name,
	                 int id,
	                 AppendageAction action)
	{
		this.name = name;
		this.id = id;

		this.action = action;
	}

	public float execute(PetriDish dish, Pye pye, int index, float amount)
	{
		return this.action.execute(dish, pye, index, amount);
	}

	public final String getName()
	{
		return this.name;
	}

	public final int getID()
	{
		return this.id;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "@" + this.id + ":" + this.name;
	}
}
