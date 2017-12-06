package net.jimboi.test.sleuth.base;

import org.zilar.console.Console;
import org.zilar.console.ConsoleStyle;

/**
 * Created by Andy on 10/7/17.
 */
public class StateMenu extends GameState
{
	@Override
	protected void onScreenStart(Console con)
	{
		ConsoleStyle.title(con, "The Sleuth Game");

		ConsoleStyle.message(con, "You wake up. Time for work.");

		ConsoleStyle.newline(con);
		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		ConsoleStyle.button(con, "New Case", () -> {
			DATA.clear();
			this.next(con, new StateCase());
		});
		ConsoleStyle.newline(con);

		if (!DATA.isEmpty())
		{
			ConsoleStyle.button(con, "Continue Case", () ->
			{
				this.next(con, new StateCase());
			});
			ConsoleStyle.newline(con);
		}

		ConsoleStyle.button(con, "Quit", con::quit);
		ConsoleStyle.newline(con);
	}
}
