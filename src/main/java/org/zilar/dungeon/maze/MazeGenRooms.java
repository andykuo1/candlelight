package org.zilar.dungeon.maze;

import org.qsilver.util.map2d.IntMap;
import org.zilar.dungeon.DungeonData;
import org.zilar.dungeon.RoomBuilder;
import org.zilar.dungeon.RoomData;
import org.zilar.dungeon.RoomDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 5/28/17.
 */
public class MazeGenRooms extends AbstractMazeGen
{
	protected final RoomBuilder roomBuilder;
	protected final RegionHandler regionHandler;
	protected final RoomDecorator roomDecorator;

	private final List<RoomData> rooms = new ArrayList<>();

	public MazeGenRooms(RoomBuilder roomBuilder, RoomDecorator roomDecorator, RegionHandler regionHandler)
	{
		this.roomBuilder = roomBuilder;
		this.roomDecorator = roomDecorator;
		this.regionHandler = regionHandler;
	}

	public RoomData addRoom(RoomData roomData)
	{
		this.rooms.removeIf((room) -> room.intersects(roomData));
		this.rooms.add(roomData);
		return roomData;
	}

	public RoomData addRandomDecoratedRoom(Random rand, int minSize, int maxSize)
	{
		RoomData nextRoom = this.roomBuilder.generate(this.regionHandler, rand, minSize, maxSize);

		for (RoomData room : this.rooms)
		{
			if (room.intersects(nextRoom))
			{
				return null;
			}
		}

		this.rooms.add(nextRoom);
		this.roomDecorator.decorate(nextRoom);
		return nextRoom;
	}

	@Override
	public void bake(DungeonData data)
	{
		IntMap tiles = data.getTiles();
		IntMap regions = data.getRegions();
		for (RoomData room : this.rooms)
		{
			IntMap.set(tiles, room.x, room.y, room.getTiles());
			IntMap.fill(regions, room.x, room.y, room.width, room.height, room.getRegionID());
		}

		this.clear();
	}

	@Override
	public void clear()
	{
		this.rooms.clear();
	}

	public RoomBuilder getRoomBuilder()
	{
		return this.roomBuilder;
	}

	public RegionHandler getRegionHandler()
	{
		return this.regionHandler;
	}
}
