package net.jimboi.boron.stage_b.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Andy on 8/14/17.
 */
public class ChatServer
{
	public static void main(String[] args)
	{
		new ChatServer(8000).run();

		System.exit(0);
	}

	private int port;

	public ChatServer(int port)
	{
		this.port = port;
	}

	public void run()
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try
		{
			ServerBootstrap bootstrap = new ServerBootstrap()
					.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChatServerInitializer());

			bootstrap.bind(this.port).sync();

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
					for(Channel channel : ChatServerHandler.CHANNELS)
					{
						channel.writeAndFlush("[SERVER] : " + line + "\n");
					}
				}

				System.out.println(" : " + line);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
