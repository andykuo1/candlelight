package net.jimboi.test.chubbycat.client;

import net.jimboi.test.chubbycat.ChubbyCat;
import net.jimboi.test.chubbycat.message.Message;

import org.bstone.console.Console;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Andy on 11/29/17.
 */
public class ChatClient implements Runnable
{
	private final Console console;
	private Queue<Message> outgoing = new LinkedBlockingQueue<>();

	public ChatClient(Console console)
	{
		this.console = console;
	}

	public void sendToServer(String msg)
	{
		this.outgoing.offer(new Message("", msg));
	}

	@Override
	public void run()
	{
		this.console.println("Starting Client...");
		EventLoopGroup boss = new NioEventLoopGroup();
		try
		{
			Bootstrap b = new Bootstrap();
			b.group(boss)
					.channel(NioSocketChannel.class)
					.handler(new ClientChannelInitializer());

			this.console.println("Connecting to Server...");
			Channel ch = b.connect(ChubbyCat.HOST, ChubbyCat.PORT).sync().channel();
			this.console.println("Connection Established!");

			ChannelFuture prevWrite;
			for(;;)
			{
				Message msg = this.outgoing.poll();
				if (msg == null)
				{
					Thread.sleep(50);
					continue;
				}

				prevWrite = ch.writeAndFlush(msg);

				if ("bye".equals(msg.getData().toLowerCase()))
				{
					ch.closeFuture().sync();
					break;
				}
			}

			if (prevWrite != null)
			{
				prevWrite.sync();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.console.println("Stopping Client...");
			boss.shutdownGracefully();
		}

		this.console.println("Connection Ended!");
	}
}
