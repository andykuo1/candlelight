package org.bstone.console.program;

import org.bstone.console.Console;
import org.zilar.console.board.Blackboard;

import java.util.Stack;

/**
 * Created by Andy on 12/3/17.
 */
public class ConsoleBoard
{
	protected final Blackboard data = new Blackboard();
	private final Stack<ConsoleProgram> states = new Stack<>();
	private ConsoleProgram currentProgram = null;

	public final void refresh(Console con)
	{
		double delay = con.writer().getTypeDelay();

		con.writer().setTypeDelay(0);
		this.execute(con, this.currentProgram);
		con.writer().setTypeDelay(delay);
	}

	public final void next(Console con, ConsoleProgram state)
	{
		if (this.currentProgram != state && this.currentProgram != null)
		{
			this.currentProgram.end(con);
			this.states.push(this.currentProgram);
			this.currentProgram = null;
		}

		this.currentProgram = state;
		if (!this.currentProgram.begin(con, this))
		{
			this.back(con);
		}
		else
		{
			this.execute(con, this.currentProgram);
		}
	}

	public final void back(Console con)
	{
		if (this.states.isEmpty())
		{
			con.stop();
		}
		else
		{
			this.currentProgram = this.states.pop();
			if (!this.currentProgram.begin(con, this))
			{
				this.back(con);
			}
			else
			{
				this.execute(con, this.currentProgram);
			}
		}
	}

	public void clear()
	{
		this.data.clear();
		this.states.clear();
	}

	protected final void execute(Console con, ConsoleProgram program)
	{
		con.clear();
		if (program != null)
		{
			program.main(con);
		}
	}

	public ConsoleProgram getCurrentProgram()
	{
		return this.currentProgram;
	}
}
