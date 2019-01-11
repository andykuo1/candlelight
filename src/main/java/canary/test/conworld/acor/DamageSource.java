package canary.test.conworld.acor;

/**
 * Created by Andy on 8/30/17.
 */
public class DamageSource
{
	private Actor owner;

	public DamageSource(Actor owner)
	{
		this.owner = owner;
	}

	public Actor getOwner()
	{
		return this.owner;
	}
}
