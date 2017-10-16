package net.jimboi.test.sleuth.base;

import net.jimboi.test.sleuth.example.Blackboard;

import org.bstone.console.Console;

/**
 * Created by Andy on 10/7/17.
 */
public abstract class GameState
{
	public static final Blackboard DATA = new Blackboard();

	private GameState prev;

	protected abstract void onScreenStart(Console con);

	protected void onScreenStop(Console con)
	{
		con.clear();
	}

	public final void start(Console con)
	{
		this.onScreenStart(con);
	}

	public final void stop(Console con)
	{
		this.onScreenStop(con);
	}

	public final void restart(Console con)
	{
		this.stop(con);
		this.start(con);
	}

	public final void next(Console con, GameState state)
	{
		this.stop(con);
		state.prev = this;
		state.start(con);
	}

	public final void back(Console con)
	{
		this.stop(con);
		if (this.prev != null)
		{
			this.prev.start(con);
		}
		else
		{
			con.quit();
		}
		this.prev = null;
	}
}
