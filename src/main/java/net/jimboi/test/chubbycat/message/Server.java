package net.jimboi.test.chubbycat.message;

import net.jimboi.test.chubbycat.ChubbyCat;
import net.jimboi.test.chubbycat.server.ServerChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Andy on 11/29/17.
 */
public class Server implements Runnable
{
	@Override
	public void run()
	{
		System.out.println("Starting Server...");
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerChannelInitializer());

			System.out.println("Connecting...");
			Channel ch = b.bind(ChubbyCat.PORT).sync().channel();
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
			System.out.println("Stopping Server...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}

		System.out.println("Connection Ended!");
	}
}
