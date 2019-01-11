package canary.test.chubbycat;

import canary.test.chubbycat.client.ChatClient;
import canary.test.chubbycat.server.ChatServer;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;
import canary.zilar.console.board.ConsoleBoard;
import canary.zilar.console.board.ConsoleState;

/**
 * Created by Andy on 11/29/17.
 */
public class MainState implements ConsoleState
{
	private ConsoleBoard board;

	@Override
	public boolean begin(Console con, ConsoleBoard board)
	{
		this.board = board;
		return true;
	}

	@Override
	public void render(Console con)
	{
		ConsoleStyle.title(con, "ChubbyCat");
		ConsoleStyle.button(con, "Client", console -> {
			console.clear();
			ChatClient client = new ChatClient(console);
			new Thread(client).start();
			console.setInputFieldHandler(client::sendToServer);
		});
		ConsoleStyle.button(con, "Server", console -> {
			console.clear();
			ChatServer server = new ChatServer(console);
			new Thread(server).start();
			console.setInputFieldHandler(server::sendToAll);
		});
		ConsoleStyle.button(con, "Exit", console -> {
			console.clear();
			console.quit();
		});
	}
}
