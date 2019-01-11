package apricot.hoob.world.agents;

import apricot.base.input.OldInputManager;
import apricot.hoob.world.World;

import java.util.function.Consumer;

/**
 * Created by Andy on 7/13/17.
 */
public class ActionAgent extends WorldAgent
{
	public Consumer<ActionAgent> actionFunction;

	public ActionAgent(World world, Consumer<ActionAgent> actionFunction)
	{
		super(world, -1);

		this.actionFunction = actionFunction;
	}

	public boolean tryActivate(float x, float y)
	{
		if (this.intersects(x, y) && OldInputManager.isInputReleased("mouseleft"))
		{
			this.actionFunction.accept(this);
			return true;
		}

		return false;
	}

	public boolean intersects(float x, float y)
	{
		return this.pos.distance(x, y) < 1F;
	}
}
