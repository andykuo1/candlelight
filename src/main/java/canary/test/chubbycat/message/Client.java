package canary.test.chubbycat.message;

import canary.test.chubbycat.client.ClientChannelInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Andy on 11/29/17.
 */
public class Client implements Runnable
{
	private final String host;
	private final int port;

	public Client(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	@Override
	public void run()
	{
		System.out.println("Starting Client...");
		EventLoopGroup boss = new NioEventLoopGroup();
		try
		{
			Bootstrap b = new Bootstrap();
			b.group(boss)
					.channel(NioSocketChannel.class)
					.handler(new ClientChannelInitializer());

			System.out.println("Connecting to Server[" + this.host + ":" + this.port + "]...");
			Channel ch = b.connect(this.host, this.port).sync().channel();
			System.out.println("Connection Established!");

			//Process stuff

			ch.closeFuture().sync();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Stopping Client...");
			boss.shutdownGracefully();
		}

		System.out.println("Connection Ended!");
	}
}
