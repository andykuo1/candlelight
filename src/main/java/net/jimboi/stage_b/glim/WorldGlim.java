package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.square.AABB;

import org.joml.Vector3f;
import org.qsilver.astar.AstarNavigator;
import org.qsilver.astar.NavigatorMap;
import org.qsilver.astar.map.NavigatorCardinalMap;
import org.qsilver.util.map2d.IntMap;
import org.zilar.dungeon.DungeonBuilder;
import org.zilar.dungeon.DungeonData;
import org.zilar.dungeon.maze.MazeDungeonBuilder;

/**
 * Created by Andy on 6/1/17.
 */
public class WorldGlim
{
	private final BoundingManager boundingManager;
	private final DungeonData data;
	private final NavigatorMap<NavigatorCardinalMap.Cell> navigatorMap;
	private boolean[] solids;

	public WorldGlim(BoundingManager boundingManager)
	{
		this.boundingManager = boundingManager;
		DungeonBuilder db = new MazeDungeonBuilder(0, 45, 45);
		this.data = db.bake();

		int tiles = 0;
		for (int y = 0; y < this.data.height; ++y)
		{
			for (int x = 0; x < this.data.width; ++x)
			{
				int tile = this.data.getTiles().get(x, y);
				if (tile == 0 && x < this.data.width - 1)
				{
					tiles++;
				}
				else if (tiles > 0)
				{
					this.boundingManager.create(new AABB(x - tiles / 2F, y + 0.5F, tiles / 2F, 0.5F));
					tiles = 0;
				}
			}
		}

		this.solids = new boolean[this.data.width * this.data.height];
		for(int i = 0; i < this.data.getTiles().size(); ++i)
		{
			this.solids[i] = this.data.getTiles().get(i) == 0;
		}
		this.navigatorMap = new NavigatorCardinalMap(this.solids, this.data.width, this.data.height);
	}

	public AstarNavigator<NavigatorCardinalMap.Cell> createNavigator()
	{
		return new AstarNavigator<>(this.navigatorMap);
	}

	public Vector3f getRandomTilePos(boolean solid)
	{
		int attempts = 0;
		int index;
		do
		{
			index = (int)(Math.random() * this.solids.length);
			attempts++;
			if (attempts > 100)
			{
				return null;
			}
		}
		while(this.solids[index] != solid);
		return new Vector3f(index % this.data.width, 0, index / this.data.width);
	}

	public boolean[] getSolids()
	{
		return this.solids;
	}

	public IntMap getMap()
	{
		return this.data.getTiles();
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
