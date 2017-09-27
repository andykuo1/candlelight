package net.jimboi.test.sleuth.jamon;

/**
 * Created by Andy on 9/26/17.
 */
public abstract class Accion
{
	public abstract String[] getRequirements(Jamon user);

	public abstract String[] getSideEffects(Jamon user);

	public abstract String[] getResult(Jamon user);

	public abstract int getCost(Jamon user);
}
