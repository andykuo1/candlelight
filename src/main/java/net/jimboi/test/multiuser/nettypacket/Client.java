package net.jimboi.test.multiuser.nettypacket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by Andy on 8/15/17.
 */
public class Client extends ChannelInitializer<SocketChannel>
{
	protected final EventLoopGroup group;

	protected final Bootstrap bootstrap;

	protected Channel channel;
	private final String host;
	private final int port;

	public Client(String host, int port)
	{
		this.host = host;
		this.port = port;

		this.group = new NioEventLoopGroup();

		this.bootstrap = new Bootstrap()
				.group(this.group)
				.channel(NioSocketChannel.class)
				.handler(this);
	}

	public void start() throws InterruptedException
	{
		this.channel = this.bootstrap.connect(this.host, this.port).sync().channel();
	}

	public void stop()
	{
		this.group.shutdownGracefully();
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		final ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("handler", new ClientHandler(this));
	}

	public Channel getChannel()
	{
		return this.channel;
	}
}
