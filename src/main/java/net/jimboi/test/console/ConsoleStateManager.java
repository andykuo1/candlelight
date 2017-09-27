package net.jimboi.test.console;

import java.util.Stack;

/**
 * Created by Andy on 9/2/17.
 */
public class ConsoleStateManager
{
	private final Console console;
	private final Stack<ConsoleState> states = new Stack<>();
	private ConsoleState state = null;

	public ConsoleStateManager(Console console)
	{
		this.console = console;
	}

	protected void update()
	{
		if (this.states.isEmpty())
		{
			if (this.state != null)
			{
				this.states.push(this.state);

				this.console.clear();
				this.state.setConsoleToState(this.console);
			}
		}
		else
		{
			ConsoleState state = this.states.peek();
			if (this.state != state)
			{
				if (this.state == null)
				{
					this.states.pop();

					if (!this.states.isEmpty())
					{
						this.console.clear();
						this.states.peek().setConsoleToState(this.console);
					}
				}
				else
				{
					this.states.push(this.state);

					this.console.clear();
					this.state.setConsoleToState(this.console);
				}
			}
		}
	}

	public void setNextState(ConsoleState state)
	{
		this.state = state;
	}

	public void exitCurrentState()
	{
		this.state = null;
	}

	public ConsoleState getCurrentState()
	{
		return this.states.peek();
	}
}
