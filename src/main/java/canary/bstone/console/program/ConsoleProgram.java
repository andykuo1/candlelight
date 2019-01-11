package canary.bstone.console.program;

import canary.bstone.console.Console;
import canary.bstone.console.state.ConsoleState;
import canary.bstone.console.style.ConsoleStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

/**
 * Created by Andy on 12/18/17.
 */
public abstract class ConsoleProgram implements ConsoleState
{
	private final String title;
	private final Map<String, Function<String[], Boolean>> commands = new HashMap<>();

	public ConsoleProgram(String title)
	{
		this.title = title;
	}

	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, this.title);

		this.content(con);

		if (con.states().getPreviousProgram() != null)
		{
			ConsoleStyle.button(con, "Back", e -> e.console.states().back(e.console));
		}
		else
		{
			ConsoleStyle.button(con, "Quit", e -> e.console.stop());
		}

		con.waitForNewInputEvent(s -> this.input(con, s));
	}

	protected boolean input(Console con, String input)
	{
		StringTokenizer tokenizer = new StringTokenizer(input, " ");
		if (!tokenizer.hasMoreTokens()) return false;

		String name = tokenizer.nextToken();
		Function<String[], Boolean> command = this.commands.get(name);
		if (command == null) return false;

		int length = tokenizer.countTokens();
		String[] args = new String[length];
		for(int i = 0; i < args.length; ++i)
		{
			args[i] = tokenizer.nextToken();
		}

		if (!command.apply(args))
		{
			con.error("Cannot process command '" + input + "'.");
			return false;
		}

		return true;
	}

	protected abstract void content(Console con);

	public void registerCommand(String name, Function<String[], Boolean> command)
	{
		this.commands.put(name, command);
	}

	public void unregisterCommand(String name)
	{
		this.commands.remove(name);
	}

	public final String getTitle()
	{
		return this.title;
	}
}
