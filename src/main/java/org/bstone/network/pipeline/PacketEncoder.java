package org.bstone.network.pipeline;

import org.bstone.network.NetworkSide;
import org.bstone.network.packet.Packet;
import org.bstone.network.packet.PacketRegistry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Andy on 11/29/17.
 */
public class PacketEncoder extends MessageToByteEncoder<Packet>
{
	private final NetworkSide destinationSide;

	private final PacketRegistry packetRegistry;

	public PacketEncoder(NetworkSide destinationSide, PacketRegistry packetRegistry)
	{
		this.destinationSide = destinationSide;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception
	{
		Integer id = this.packetRegistry.getPacketID(this.destinationSide, msg);

		if (id == null) throw new IllegalArgumentException("cannot serialize unregistered packet");

		VarInt21FrameEncoder.writeVarInt(id, out);

		try
		{
			msg.encodeBytes(out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
