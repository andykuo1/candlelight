package net.jimboi.test.multiuser;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Andy on 8/14/17.
 */
public class Client
{
	public static void main(String[] args)
	{
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);

		bootstrap.handler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ch.pipeline().addLast(new TimeStampEncoder(), new TimeStampDecoder(), new ClientHandler());
			}
		});

		String SERVER_IP = "192.168.1.89";
		bootstrap.connect(SERVER_IP, 19000);
	}
}
