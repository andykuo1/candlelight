package net.jimboi.test.sleuth.queso;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.sleuth.cluedo.Cluedo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Andy on 9/26/17.
 */
public class WoodChopGame extends Cluedo
{
	@Override
	protected void create(Random rand)
	{

	}

	@Override
	protected void play(Console out)
	{
		ConsoleHelper.message(out, "Welcome to Wood Chopping Simulator!");

		QuesoMachina planner = new QuesoMachina();
		EstadoMachina states = new EstadoMachina();
		states.set("available axe");
		states.set("shining sun");
		states.set("not has an axe");
		Queue<Accion> sequence = new LinkedList<>();
		ConsoleHelper.message(out, "Start!");
		planner.plan(new GolBase("make firewood"), states, sequence);
		ConsoleHelper.message(out, sequence.toString());
	}
}
