package net.jimboi.boron.stage_b.nettypacket;

import net.jimboi.boron.stage_b.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by Andy on 8/15/17.
 */
public class Server extends ChannelInitializer<SocketChannel>
{
	protected final EventLoopGroup masterGroup;
	protected final EventLoopGroup workerGroup;

	protected final ServerBootstrap bootstrap;

	protected ChannelGroup channels;
	private final int port;

	public Server(int port)
	{
		this.port = port;

		this.masterGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();

		this.bootstrap = new ServerBootstrap()
				.group(this.masterGroup, this.workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(this);
	}

	public void start() throws InterruptedException
	{
		this.bootstrap.bind(this.port).sync();

		this.channels = new DefaultChannelGroup("clients", GlobalEventExecutor.INSTANCE);
	}

	public void stop()
	{
		this.masterGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		final ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("handler", new ServerHandler());
	}

	public ChannelGroup getChannels()
	{
		return this.channels;
	}
}
