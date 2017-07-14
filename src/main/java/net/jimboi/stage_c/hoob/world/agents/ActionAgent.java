package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.world.World;

import org.bstone.input.InputManager;

import java.util.function.Consumer;

/**
 * Created by Andy on 7/13/17.
 */
public class ActionAgent extends WorldAgent
{
	public Consumer<ActionAgent> actionFunction;

	public ActionAgent(World world, Consumer<ActionAgent> actionFunction)
	{
		super(world);

		this.actionFunction = actionFunction;
	}

	public boolean tryActivate(float x, float y)
	{
		if (this.intersects(x, y) && InputManager.isInputReleased("mouseleft"))
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
