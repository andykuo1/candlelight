package org.zilar.dungeon.maze;

import org.qsilver.util.map2d.IntMap;
import org.zilar.dungeon.DungeonBuilder;
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
public class MazeDungeonBuilder extends DungeonBuilder
{
	private final DungeonData data;
	private final List<RoomData> rooms = new ArrayList<>();

	public MazeDungeonBuilder(long seed, int width, int height)
	{
		super(seed, width, height);

		this.data = new DungeonData(this.width, this.height);
	}

	@Override
	public DungeonData bake()
	{
		IntMap.fill(this.data.getTiles(), 0, 0, this.data.width, this.data.height, 0);
		IntMap.fill(this.data.getRegions(), 0, 0, this.data.width, this.data.height, 0);

		int i;
		int centerX = this.data.width / 2;
		int centerY = this.data.height / 2;
		long seed = 0;

		Random rand = new Random(seed);
		RoomBuilder roomBuilder = new RoomBuilder(0, 0, this.data.width, this.data.height);
		CorridorBuilder corridorBuilder = new CorridorBuilder();
		RegionHandler regionHandler = new RegionHandler();
		RoomDecorator spawnDecorator = new RoomDecorator();
		RoomDecorator roomDecorator = new RoomDecorator();

		//Rooms
		{
			int numOfRoomAttempts = 20;
			int minRoomSize = 2;
			int maxRoomSize = 9;

			MazeGenRooms genRooms = new MazeGenRooms(roomBuilder, roomDecorator, regionHandler);

			//Spawn Room
			{
				RoomData spawnRoom = new RoomData(regionHandler.getNextAvailableRegion(), centerX - 3, centerY - 3, 7, 7);
				spawnDecorator.decorate(spawnRoom);
				spawnRoom.getTiles().set(3, 3, 2);
				genRooms.addRoom(spawnRoom);
			}

			for (i = 0; i < numOfRoomAttempts; ++i)
			{
				genRooms.addRandomDecoratedRoom(rand, minRoomSize, maxRoomSize);
			}

			genRooms.bake(this.data);
		}

		//Generate Maze
		{
			MazeGenMaze genMaze = new MazeGenMaze(regionHandler, corridorBuilder, this.data.getTiles(), 0.75F, (tile) -> tile == 0);
			genMaze.generate(this.rand);
			genMaze.bake(this.data);
		}

		//Merge Regions
		{
			MazeGenMergeRegions genMergeRegions = new MazeGenMergeRegions(this.rand, regionHandler, 0.08F, (tile) -> tile == 0, () -> 8);
			genMergeRegions.bake(this.data);
		}

		//Make Maze Sparse
		{
			MazeGenSparseMaze genSparseMaze = new MazeGenSparseMaze(this.rand, (tile) -> tile == 0);
			genSparseMaze.bake(this.data);
		}

		return this.data;
	}
}
