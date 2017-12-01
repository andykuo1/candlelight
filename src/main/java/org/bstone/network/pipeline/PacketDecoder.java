package org.bstone.network.pipeline;

import org.bstone.network.NetworkSide;
import org.bstone.network.packet.Packet;
import org.bstone.network.packet.PacketRegistry;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by Andy on 11/29/17.
 */
public class PacketDecoder extends ByteToMessageDecoder
{
	private final NetworkSide destinationSide;

	private final PacketRegistry packetRegistry;

	public PacketDecoder(NetworkSide destinationSide, PacketRegistry packetRegistry)
	{
		this.destinationSide = destinationSide;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		if (in.readableBytes() != 0)
		{
			int i = VarInt21FrameDecoder.readVarInt21(in);
			Packet packet = this.packetRegistry.getPacket(this.destinationSide, i);

			if (packet == null) throw new IllegalStateException("unable to create packet with id '" + i + "' for " + this.destinationSide);

			packet.decodeBytes(in);

			if (in.readableBytes() > 0)
			{
				throw new IllegalStateException("found unused remaining bytes while reading packet with id '" + i + "' for " + this.destinationSide);
			}
			else
			{
				out.add(packet);
			}
		}
	}
}
