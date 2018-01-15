package org.bstone.network;

import org.bstone.application.kernel.Engine;
import org.bstone.network.handler.Server;
import org.bstone.network.packet.PacketRegistry;
import org.bstone.network.pipeline.PacketDecoder;
import org.bstone.network.pipeline.PacketEncoder;
import org.bstone.network.pipeline.VarInt21FrameDecoder;
import org.bstone.network.pipeline.VarInt21FrameEncoder;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Andy on 12/1/17.
 */
public class NetworkEngine implements Engine
{
	private final List<ChannelFuture> endpoints = new ArrayList<>();
	private final List<NetworkDispatcher> dispatchers = new ArrayList<>();

	private final PacketRegistry packetRegistry = new PacketRegistry();

	private final Server server;

	public NetworkEngine(Server server)
	{
		this.server = server;
	}

	@Override
	public boolean initialize()
	{
		this.server.initialize();
		return true;
	}

	@Override
	public void update()
	{
		Iterator<NetworkDispatcher> iter = this.dispatchers.iterator();

		while(iter.hasNext())
		{
			final NetworkDispatcher dispatcher = iter.next();

			if (!dispatcher.isChannelOpen())
			{
				try
				{
					dispatcher.process();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				iter.remove();
			}
		}
	}

	@Override
	public void terminate()
	{
		this.server.terminate();
		this.closeEndpoints();
	}

	public void addPublicEndpoint(InetAddress address, int port)
	{
		synchronized (this.endpoints)
		{
			final EventLoopGroup boss = new NioEventLoopGroup();
			final EventLoopGroup worker = new NioEventLoopGroup();
			final ServerBootstrap b = new ServerBootstrap()
					.group(boss, worker)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception
						{
							final PacketRegistry registry = NetworkEngine.this.packetRegistry;
							final NetworkDispatcher dispatch = new NetworkDispatcher(
									NetworkEngine.this, NetworkSide.SERVER);
							NetworkEngine.this.dispatchers.add(dispatch);

							ch.pipeline()
									.addLast(new VarInt21FrameDecoder())
									.addLast(new PacketDecoder(NetworkSide.SERVER, registry))
									.addLast(new VarInt21FrameEncoder())
									.addLast(new PacketEncoder(NetworkSide.CLIENT, registry))
									.addLast(dispatch);
						}
					})
					.channel(NioServerSocketChannel.class);

			//Host and wait until done
			b.localAddress(address, port).bind().syncUninterruptibly();
		}
	}

	public SocketAddress addLocalEndpoint()
	{
		ChannelFuture ch;

		synchronized (this.endpoints)
		{
			final EventLoopGroup boss = new NioEventLoopGroup();
			final EventLoopGroup worker = new NioEventLoopGroup();
			final ServerBootstrap b = new ServerBootstrap()
					.group(boss, worker)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception
						{
							final NetworkDispatcher dispatch = new NetworkDispatcher(
									NetworkEngine.this, NetworkSide.SERVER);
							NetworkEngine.this.dispatchers.add(dispatch);

							ch.pipeline().addLast(dispatch);
						}
					})
					.channel(LocalServerChannel.class);

			//Host and wait until done
			ch = b.localAddress(LocalAddress.ANY).bind().syncUninterruptibly();
			this.endpoints.add(ch);
		}

		return ch.channel().localAddress();
	}

	public NetworkDispatcher connectToPublic(InetAddress address, int port)
	{
		NetworkDispatcher dispatch = new NetworkDispatcher(this, NetworkSide.CLIENT);

		final EventLoopGroup boss = new NioEventLoopGroup();
		final Bootstrap b = new Bootstrap()
				.group(boss)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception
					{
						final PacketRegistry registry = NetworkEngine.this.packetRegistry;

						ch.pipeline()
								.addLast(new VarInt21FrameDecoder())
								.addLast(new PacketDecoder(NetworkSide.CLIENT,
										registry))
								.addLast(new VarInt21FrameEncoder())
								.addLast(new PacketEncoder(NetworkSide.SERVER,
										registry))
								.addLast(dispatch);
					}
				})
				.channel(NioSocketChannel.class);

		//Connect and wait until done
		b.connect(address, port).syncUninterruptibly();

		return dispatch;
	}

	public NetworkDispatcher connectToLocal(SocketAddress address)
	{
		NetworkDispatcher dispatch = new NetworkDispatcher(this, NetworkSide.CLIENT);

		final EventLoopGroup boss = new DefaultEventLoopGroup();
		final Bootstrap b = new Bootstrap()
				.group(boss)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception
					{
						ch.pipeline().addLast(dispatch);
					}
				})
				.channel(LocalChannel.class);

		//Connect and wait until done
		b.connect(address).syncUninterruptibly();

		return dispatch;
	}

	public void closeEndpoints()
	{
		for(ChannelFuture ch : this.endpoints)
		{
			try
			{
				ch.channel().close().sync();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public PacketRegistry getPacketRegistry()
	{
		return this.packetRegistry;
	}
}
