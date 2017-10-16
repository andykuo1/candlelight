package net.jimboi.test.sleuth.base;

import org.bstone.console.Console;
import org.bstone.console.ConsoleUtil;

/**
 * Created by Andy on 10/7/17.
 */
public class StateMenu extends GameState
{
	@Override
	protected void onScreenStart(Console con)
	{
		ConsoleUtil.title(con, "The Sleuth Game");

		ConsoleUtil.message(con, "You wake up. Time for work.");

		ConsoleUtil.newline(con);
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		ConsoleUtil.button(con, "New Case", () -> {
			DATA.clear();
			this.next(con, new StateCase());
		});
		ConsoleUtil.newline(con);

		if (!DATA.isEmpty())
		{
			ConsoleUtil.button(con, "Continue Case", () ->
			{
				this.next(con, new StateCase());
			});
			ConsoleUtil.newline(con);
		}

		ConsoleUtil.button(con, "Quit", con::quit);
		ConsoleUtil.newline(con);
	}
}
