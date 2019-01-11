package canary.bstone.network.packet;

/**
 * Created by Andy on 11/29/17.
 */
public abstract class PacketHandler<T extends Packet>
{
	public abstract Packet processPacket(PacketContext ctx, T packet);
}
