package org.bstone.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * Created by Andy on 11/29/17.
 */
public abstract class Packet
{
	public abstract void encodeBytes(ByteBuf dst) throws Exception;

	public abstract void decodeBytes(ByteBuf src) throws Exception;
}
