package net.jimboi.test.conworld;

import net.jimboi.test.conworld.battle.BattleState;
import net.jimboi.test.conworld.world.World;

import org.zilar.console.Console;

import java.util.Stack;

/**
 * Created by Andy on 8/27/17.
 */
public class BattleMain
{
	private static Console CONSOLE;
	private static BattleMain INSTANCE;
	private static World WORLD;

	public static final BattleMain getInstance()
	{
		return INSTANCE;
	}
	public static final Console getConsole()
	{
		return CONSOLE;
	}
	public static final World getWorld()
	{
		return WORLD;
	}

	private static volatile boolean RUNNING = false;

	public static void main(String[] args)
	{
		CONSOLE = new Console(320, 480);

		CONSOLE.setWindowCloseHandler(console ->
		{
			RUNNING = false;
			return false;
		});

		INSTANCE = new BattleMain();
		WORLD = new World();

		RUNNING = true;

		while (RUNNING)
		{
			INSTANCE.update();
		}

		CONSOLE.destroy();

		System.exit(0);
	}

	public static void stop()
	{
		RUNNING = false;
	}

	private final Stack<BattleState> states = new Stack<>();
	private BattleState state = null;

	public void update()
	{
		if (this.states.isEmpty())
		{
			if (this.state != null)
			{
				this.states.push(this.state);

				CONSOLE.clear();
				this.state.initialize();
			}
		}
		else
		{
			BattleState state = this.states.peek();
			if (this.state != state)
			{
				if (this.state == null)
				{
					this.states.pop();

					if (!this.states.isEmpty())
					{
						CONSOLE.clear();
						this.states.peek().initialize();
					}
				}
				else
				{
					this.states.push(this.state);

					CONSOLE.clear();
					this.state.initialize();
				}
			}
		}
	}

	public void setNextState(BattleState state)
	{
		this.state = state;
	}

	public void exitState()
	{
		this.state = null;
	}

	public BattleState getCurrentState()
	{
		return this.states.peek();
	}
}
