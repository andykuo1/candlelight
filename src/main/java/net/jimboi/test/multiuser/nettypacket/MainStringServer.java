package net.jimboi.test.multiuser.nettypacket;

import io.netty.channel.Channel;

/**
 * Created by Andy on 8/15/17.
 */
public class MainStringServer
{
	public static void main(String[] args)
	{
		Game game = new Game();
		Server server = new Server(8000);
		try
		{
			server.start();

			game.commandManager.register("say", new Command()
			{
				@Override
				public boolean process(String[] args)
				{
					for(Channel channel : server.getChannels())
					{
						channel.writeAndFlush(args[0]);
					}
					return true;
				}
			});

			game.run();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			server.stop();
		}
	}
}
