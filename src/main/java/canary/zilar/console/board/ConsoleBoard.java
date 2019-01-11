package canary.zilar.console.board;

import canary.zilar.console.Console;

import java.util.Stack;

/**
 * Created by Andy on 10/7/17.
 */
public class ConsoleBoard
{
	protected final Blackboard data = new Blackboard();
	private final Stack<ConsoleState> states = new Stack<>();
	private ConsoleState currentState = this::onStart;

	public void onStart(Console con) {}

	public final void start(Console con)
	{
		this.data.clear();
		this.states.clear();

		this.onStart(con);
	}

	public final void refresh(Console con)
	{
		boolean flag = con.isTypeWriterMode();

		con.setTypeWriterMode(false);
		this.render(con, this.currentState);
		con.setTypeWriterMode(flag);
	}

	public final void next(Console con, ConsoleState state)
	{
		if (this.currentState != state && this.currentState != null)
		{
			this.currentState.end(con);
			this.states.push(this.currentState);
			this.currentState = null;
		}

		this.currentState = state;
		if (!this.currentState.begin(con, this))
		{
			this.back(con);
		}
		else
		{
			this.render(con, this.currentState);
		}
	}

	public final void back(Console con)
	{
		if (this.states.isEmpty())
		{
			con.quit();
		}
		else
		{
			this.currentState = this.states.pop();
			if (!this.currentState.begin(con, this))
			{
				this.back(con);
			}
			else
			{
				this.render(con, this.currentState);
			}
		}
	}

	protected final void render(Console con, ConsoleState state)
	{
		con.clear();
		state.render(con);
	}
}
