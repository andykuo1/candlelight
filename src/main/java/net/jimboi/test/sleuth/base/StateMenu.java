package net.jimboi.test.sleuth.base;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleUtil;

/**
 * Created by Andy on 10/7/17.
 */
public class StateMenu extends GameState
{
	public StateMenu(Game game)
	{
		super(game);
	}

	@Override
	protected void onScreenStart(Console con)
	{
		ConsoleUtil.title(con, "The Sleuth Game");

		ConsoleUtil.message(con, "You wake up. Time for work.");

		ConsoleUtil.newline(con);
		ConsoleUtil.divider(con, "- ");
		ConsoleUtil.newline(con);

		ConsoleUtil.button(con, "New Case", () -> {
			this.game.clear();
			this.next(con, new StateCase(this.game));
		});
		ConsoleUtil.newline(con);

		if (!this.game.isEmpty())
		{
			ConsoleUtil.button(con, "Continue Case", () ->
			{
				this.next(con, new StateCase(this.game));
			});
			ConsoleUtil.newline(con);
		}

		ConsoleUtil.button(con, "Quit", con::quit);
		ConsoleUtil.newline(con);
	}
}
