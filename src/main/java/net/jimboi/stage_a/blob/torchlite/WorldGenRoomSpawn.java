package net.jimboi.stage_a.blob.torchlite;

import java.util.Random;

public class WorldGenRoomSpawn extends WorldGenRoom
{
	protected final int spawnTile;

	public WorldGenRoomSpawn(int floorTile, int spawnTile, Recti room, int region)
	{
		super(floorTile, room, region);
		this.spawnTile = spawnTile;
		this.setCheckIntersection(false);
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		boolean flag = super.generate(rand, world);
		while (!flag) {}

		Recti room = this.room;
		if (room.width > 0 && room.height > 0)
		{
			room.inflate(-1);
		}
		Vec2i vec = new Vec2i(room.x + rand.nextInt(room.width), room.y + rand.nextInt(room.height));

		world.map.set(vec.x, vec.y, this.spawnTile);
		world.spawns.add(vec);

		return true;
	}

	@Override
	public int getWeight()
	{
		return 0;
	}
}
