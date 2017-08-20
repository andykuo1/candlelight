package net.jimboi.boron.stage_b.packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 8/14/17.
 */
public class PacketHandler
{
	private final BlockingQueue<Packet> packets = new LinkedBlockingQueue<>();
	private final PacketContext context;

	public PacketHandler(PacketContext context)
	{
		this.context = context;
	}

	public void sendToAll(Packet packet)
	{

	}

	public void sendToServer(Packet packet)
	{

	}

	public void processServerPacket(Packet packet, PacketContext context)
	{

	}

	public void processClientPacket(Packet packet, PacketContext context)
	{

	}
}
