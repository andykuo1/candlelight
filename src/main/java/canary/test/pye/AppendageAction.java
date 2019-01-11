package canary.test.pye;

/**
 * Created by Andy on 10/17/17.
 */
@FunctionalInterface
public interface AppendageAction
{
	float execute(PetriDish world, Pye owner, int index, float amt);
}
