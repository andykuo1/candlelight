package net.jimboi.boron.stage_b.packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 8/19/17.
 */
public class ServerPacketHandler
{
	protected final BlockingQueue<Packet> packets = new LinkedBlockingQueue<>();

	public void sendToClient(Packet packet, String client)
	{

	}

	public void sendToAll(Packet packet)
	{

	}

	public void process(Packet packet)
	{

	}
}
