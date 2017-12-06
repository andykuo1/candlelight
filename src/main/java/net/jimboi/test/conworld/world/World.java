package net.jimboi.test.conworld.world;

import net.jimboi.test.conworld.BattleMain;
import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.battle.BattleStateMenu;
import net.jimboi.test.conworld.view.TurnView;

import org.zilar.console.Console;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 8/27/17.
 */
public class World
{
	public final Set<Actor> actors = new HashSet<>();

	public final Set<Actor> activeActors = new HashSet<>();
	public TurnView turnView;

	public World()
	{
		this.actors.add(new Actor("Player"));
		this.actors.add(new Actor("Zombie"));

		BattleMain.getInstance().setNextState(new BattleStateMenu(this));
	}

	public void nextTurn()
	{
		if (this.turnView != null)
		{
			this.turnView.destroy();
			this.turnView = null;
		}
		else
		{
			this.activeActors.clear();
			this.activeActors.addAll(this.actors);
		}

		if (!this.activeActors.isEmpty())
		{
			Iterator<Actor> iter = this.activeActors.iterator();
			Actor current = iter.next();
			iter.remove();

			this.turnView = new TurnView(new Console(320, 480), this, current);
			this.turnView.create();
		}
	}

	public Console getCurrentConsole()
	{
		return this.turnView == null ? BattleMain.getConsole() : this.turnView.getConsole();
	}
}
