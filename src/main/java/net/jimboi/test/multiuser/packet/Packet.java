package net.jimboi.test.multiuser.packet;

import java.nio.ByteBuffer;

/**
 * Created by Andy on 8/14/17.
 */
public abstract class Packet
{
	public abstract void writeToByteBuffer(ByteBuffer buffer);

	public abstract void readFromByteBuffer(ByteBuffer buffer);

	public final int getID()
	{
		return this.getClass().hashCode();
	}
}
