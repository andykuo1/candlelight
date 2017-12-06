package net.jimboi.test.chubbycat.server;

import net.jimboi.test.chubbycat.ChubbyCat;
import net.jimboi.test.chubbycat.message.Message;

import org.zilar.console.Console;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Andy on 11/29/17.
 */
public class ChatServer implements Runnable
{
	private final Console console;
	private Queue<Message> outgoing = new LinkedBlockingQueue<>();

	public ChatServer(Console console)
	{
		this.console = console;
	}

	public void sendToAll(String msg)
	{
		this.outgoing.offer(new Message("server", msg));
	}

	@Override
	public void run()
	{
		this.console.println("Starting Server...");
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerChannelInitializer());

			this.console.println("Connecting...");
			Channel ch = b.bind(ChubbyCat.PORT).sync().channel();
			this.console.println("Connection Established!");

			for(;;)
			{
				Message msg = this.outgoing.poll();
				if (msg == null)
				{
					Thread.sleep(50);
					continue;
				}

				for (Channel c : ServerHandler.CHANNELS)
				{
					c.writeAndFlush(msg);
				}

				this.console.println(msg.toString());

				if ("bye".equals(msg.getData().toLowerCase()))
				{
					ch.closeFuture().sync();
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.console.println("Stopping Server...");
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}

		this.console.println("Connection Ended!");
	}
}
