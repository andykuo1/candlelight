package net.jimboi.test.sleuth.queso;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andy on 9/26/17.
 */
public class QuesoMachina
{
	public void plan(Gol goal, EstadoMachina states, Queue<Accion> dst)
	{
		//AstarNavigator<> navigator;

	}

	public void step(Gol goal, EstadoMachina states, Queue<Accion> actions, Queue<Accion> dst, int cost)
	{

		//Get all available actions
		//Queue<Accion> actions = new LinkedList<>();
		//Current state...
		while (!actions.isEmpty())
		{
			Accion action = actions.poll();
			if (action.hasRequiredConditions(states))
			{
				//Can use this action!
				dst.add(action);
				action.apply(states);
			}
		}



		if (goal.hasConditions(states))
		{
			//Calculate cost

		}

		while(!actions.isEmpty())
		{
			Accion next = actions.poll();
			if (next.hasRequiredConditions(states))
			{
				dst.add(next);
				next.apply(states);

				Queue<Accion> nexts = new LinkedList<>();
				nexts.addAll(actions);
				this.step(goal, states, nexts, dst, cost + next.getCost());
			}
		}
	}
}
