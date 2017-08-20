package net.jimboi.boron.stage_b.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Andy on 8/14/17.
 */
public class ChatClient
{
	public static void main(String[] args)
	{
		new ChatClient("192.168.1.89", 8000).run();

		System.exit(0);
	}

	private String host;
	private int port;

	public ChatClient(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public void run()
	{
		EventLoopGroup group = new NioEventLoopGroup();
		try
		{
			//Setup bootstrap
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChatClientInitializer());

			//Establish connection
			Channel channel = bootstrap.connect(this.host, this.port).sync().channel();

			//Read input
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			boolean running = true;
			while(running)
			{
				String line = in.readLine();
				if ("stop".equals(line))
				{
					running = false;
				}
				else
				{
					channel.writeAndFlush(line + "\n");
				}

				System.out.println(" : " + line);
			}
		}
		catch (InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			group.shutdownGracefully();
		}
	}
}
