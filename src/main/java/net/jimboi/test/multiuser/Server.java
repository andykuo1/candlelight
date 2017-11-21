package net.jimboi.test.multiuser;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by Andy on 8/14/17.
 */
public class Server
{
	public static void main(String[] args) throws Exception
	{
		NioEventLoopGroup boosGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boosGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);

		final EventExecutorGroup group = new DefaultEventExecutorGroup(1500);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, 5));
				pipeline.addLast(new TimeStampEncoder());
				pipeline.addLast(new TimeStampDecoder());

				pipeline.addLast(group, "serverHandler", new ServerHandler());
			}
		});

		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.bind(19000).sync();
	}
}
