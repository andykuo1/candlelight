package net.jimboi.boron.stage_b.nettypacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Andy on 8/15/17.
 */
public class Game
{
	public final CommandManager commandManager = new CommandManager();
	private boolean running = false;

	public Game()
	{
		this.commandManager.register("stop", new Command()
		{
			@Override
			public boolean process(String[] args)
			{
				Game.this.running = false;
				return true;
			}
		});
	}

	public void stop()
	{
		this.running = false;
	}

	public void run()
	{
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		this.running = true;
		while(this.running)
		{
			try
			{
				final String line = reader.readLine();
				if (line != null)
				{
					if (line.startsWith("/"))
					{
						String cmd;
						String[] args;
						int argi = line.indexOf(' ');

						if (argi == -1)
						{
							cmd = line.substring(1);
							args = null;
						}
						else
						{
							cmd = line.substring(1, argi);
							args = line.substring(argi + 1).split(" ");
						}

						Command command = commandManager.getCommand(cmd);
						if (command != null)
						{
							command.process(args);
						}
					}
					else
					{
						Command command = commandManager.getCommand("say");
						if (command != null)
						{
							command.process(new String[] {line});
						}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning()
	{
		return this.running;
	}
}
