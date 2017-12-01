package org.bstone.network;

import org.bstone.network.packet.Packet;

import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.TimeoutException;

/**
 * Created by Andy on 11/29/17.
 */
public class NetworkDispatcher extends SimpleChannelInboundHandler<Packet>
{
	private final Queue<Packet> outgoing = new LinkedBlockingQueue<>();
	private final NetworkSide side;

	private final NetworkEngine network;

	private Channel channel;
	private SocketAddress address;

	private String closeMessage;

	NetworkDispatcher(NetworkEngine network, NetworkSide side)
	{
		this.network = network;
		this.side = side;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelActive(ctx);

		this.channel = ctx.channel();
		this.address = this.channel.remoteAddress();

		//Try to handshake?
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		this.closeChannel("endofstream");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		String msg;

		if (cause instanceof TimeoutException)
		{
			msg = "timeout";
		}
		else
		{
			msg = "unknown";
		}

		this.closeChannel(msg);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception
	{
		if (this.channel.isOpen())
		{
			try
			{
				//TODO: Process the received packet
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void process()
	{
		this.flushOutgoingPackets();

		//TODO: Update listeners

		this.flushIncomingPackets();
	}

	public void closeChannel(String msg)
	{
		if (this.channel.isOpen())
		{
			this.channel.close().awaitUninterruptibly();
			this.closeMessage = msg;
		}
	}

	public void sendPacket(Packet packet)
	{
		if (this.channel.isOpen())
		{
			//Send the packet
			this.flushOutgoingPackets();
			this.dispatchPacket(packet);
		}
		else
		{
			//Store the packet
			this.outgoing.offer(packet);
		}
	}

	protected void dispatchPacket(Packet packet)
	{
		//If in its own thread...
		if (this.channel.eventLoop().inEventLoop())
		{
			this.dispatchPacketInEventLoop(packet);
		}
		//In another thread...
		else
		{
			this.channel.eventLoop().execute(() -> this.dispatchPacketInEventLoop(packet));
		}
	}

	private ChannelFuture dispatchPacketInEventLoop(Packet packet)
	{
		return this.channel.writeAndFlush(packet);
	}

	protected void flushIncomingPackets()
	{
		this.channel.flush();
	}

	protected void flushOutgoingPackets()
	{
		while (!this.outgoing.isEmpty())
		{
			Packet packet = this.outgoing.poll();
			this.dispatchPacket(packet);
		}
	}

	public boolean isChannelOpen()
	{
		return this.channel != null && this.channel.isOpen();
	}

	public SocketAddress getRemoteAddress()
	{
		return this.address;
	}

	public NetworkSide getNetworkSide()
	{
		return this.side;
	}

	public NetworkEngine getNetwork()
	{
		return this.network;
	}
}
