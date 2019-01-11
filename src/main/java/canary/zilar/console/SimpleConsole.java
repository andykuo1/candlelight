package canary.zilar.console;

import canary.zilar.console.board.ConsoleBoard;
import canary.zilar.console.board.ConsoleState;

/**
 * Created by Andy on 11/29/17.
 */
public class SimpleConsole extends Console
{
	private final ConsoleBoard board;
	private final ConsoleState init;

	public SimpleConsole(int width, int height, ConsoleState init)
	{
		super(width, height);

		this.board = new ConsoleBoard();
		this.init = init;

		this.addCommand("back", s -> this.board.back(this));
	}

	@Override
	public void run()
	{
		this.board.start(this);
		this.board.next(this, this.init);

		super.run();
	}

	public final ConsoleBoard getBoard()
	{
		return this.board;
	}
}
