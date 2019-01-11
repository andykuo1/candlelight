package canary.bstone.network.packet;

import canary.bstone.network.NetworkSide;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 11/30/17.
 */
public class PacketRegistry
{
	private final Map<NetworkSide, Map<Integer, RegistryPacketBundle<? extends Packet>>> packets = new HashMap<>();
	private final Map<NetworkSide, Map<Class<? extends Packet>, Integer>> ids = new HashMap<>();

	private int nextAvailablePacketID = 0;

	public <T extends Packet> void registerPacket(NetworkSide destinationSide, Class<T> packetClass, Class<PacketHandler<T>> handlerClass)
	{
		Map<Integer, RegistryPacketBundle<? extends Packet>> packets =
				this.packets.computeIfAbsent(destinationSide,
				networkSide -> new HashMap<>());
		Map<Class<? extends Packet>, Integer> ids =
				this.ids.computeIfAbsent(destinationSide,
				networkSide -> new HashMap<>());

		final int id = this.getNextAvailablePacketID();
		RegistryPacketBundle<T> bundle = new RegistryPacketBundle<>(packetClass, handlerClass);
		packets.put(id, bundle);
		ids.put(packetClass, id);
	}

	public Integer getPacketID(NetworkSide destinationSide, Packet packet)
	{
		return this.ids.get(destinationSide).get(packet.getClass());
	}

	public Packet getPacket(NetworkSide destinationSide, int packetID) throws InstantiationException, IllegalAccessException
	{
		RegistryPacketBundle<? extends Packet> bundle = this.packets.get(destinationSide).get(packetID);
		return bundle != null ? bundle.packetClass.newInstance() : null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Packet> PacketHandler<T> getPacketHandler(NetworkSide destinationSide, T packet) throws InstantiationException, IllegalAccessException
	{
		Integer id = this.getPacketID(destinationSide, packet);
		if (id == null) throw new IllegalArgumentException("cannot find handler for unregistered packet");

		RegistryPacketBundle bundle = this.packets.get(destinationSide).get(id);
		if (bundle != null && bundle.packetClass.equals(packet.getClass()))
		{
			return ((Class<PacketHandler<T>>) bundle.handlerClass).newInstance();
		}

		return null;
	}

	protected int getNextAvailablePacketID()
	{
		return this.nextAvailablePacketID++;
	}

	public void clear()
	{
		this.packets.clear();
		this.ids.clear();
	}

	private static final class RegistryPacketBundle<T extends Packet>
	{
		final Class<T> packetClass;
		final Class<PacketHandler<T>> handlerClass;

		RegistryPacketBundle(Class<T> packetClass, Class<PacketHandler<T>> handlerClass)
		{
			this.packetClass = packetClass;
			this.handlerClass = handlerClass;
		}
	}
}
