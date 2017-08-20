package net.jimboi.boron.stage_b.nettypacket;

/**
 * Created by Andy on 8/15/17.
 */
public class MainStringClient
{
	public static void main(String[] args)
	{
		Game game = new Game();
		Client client = new Client("localhost", 8000);
		try
		{
			client.start();

			game.commandManager.register("say", new Command()
			{
				@Override
				public boolean process(String[] args)
				{
					client.getChannel().writeAndFlush(args[0]);
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
			client.stop();
		}
	}
}
