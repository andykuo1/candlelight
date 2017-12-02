package net.jimboi.test.chubbycat.client;

import net.jimboi.test.chubbycat.message.MessageDecoder;
import net.jimboi.test.chubbycat.message.MessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

/**
 * Created by Andy on 11/29/17.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast(new MessageDecoder());
		pipeline.addLast(new MessageEncoder());

		pipeline.addLast(new ClientHandler());
	}
}
