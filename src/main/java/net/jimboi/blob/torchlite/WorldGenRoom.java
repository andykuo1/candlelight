package net.jimboi.blob.torchlite;

import java.util.Random;

public class WorldGenRoom extends AbstractWorldGen
{
	protected final int floorTile;
	protected final Recti room;
	protected final int region;

	protected boolean checkIntersection = true;

	public WorldGenRoom(int floorTile, Recti room, int region)
	{
		this.floorTile = floorTile;
		this.room = room;
		this.region = region;
	}

	public WorldGenRoom setCheckIntersection(boolean checkIntersection)
	{
		this.checkIntersection = checkIntersection;
		return this;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		if (world.map.isValid(this.room.x, this.room.y) && world.map.isValid(this.room.x + this.room.width, this.room.y + this.room.height))
		{
			if (this.checkIntersection)
			{
				for(Recti r : world.rooms)
				{
					if (this.room.intersects(r)) return false;
				}
			}

			world.rooms.add(this.room);

			Vec2Iterator iter = new Vec2Iterator(this.room);
			while(iter.hasNext())
			{
				Vec2i pos = iter.next();
				world.map.set(pos.x, pos.y, this.floorTile);
				world.region.set(pos.x, pos.y, this.region);
			}

			return true;
		}

		return false;
	}

	@Override
	public int getWeight()
	{
		return Integer.MIN_VALUE;
	}
}
