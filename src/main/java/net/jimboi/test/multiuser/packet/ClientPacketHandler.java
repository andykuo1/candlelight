package net.jimboi.test.multiuser.packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Andy on 8/19/17.
 */
public class ClientPacketHandler
{
	protected final BlockingQueue<Packet> packets = new LinkedBlockingQueue<>();

	public void sendToServer(Packet packet)
	{

	}

	public void process(Packet packet)
	{

	}
}
