package net.jimboi.glim;

import net.jimboi.glim.bounding.BoundingManager;
import net.jimboi.glim.bounding.square.AABB;
import net.jimboi.mod2.dungeon.DungeonBuilder;
import net.jimboi.mod2.dungeon.DungeonData;
import net.jimboi.mod2.dungeon.maze.MazeDungeonBuilder;

import org.bstone.util.map2d.IntMap;
import org.joml.Vector3fc;

/**
 * Created by Andy on 6/1/17.
 */
public class WorldGlim
{
	private final BoundingManager boundingManager;
	private final DungeonData data;

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
	}

	public boolean intersects(Vector3fc pos)
	{
		if (pos.y() <= 1 && pos.y() >= 0)
		{
			int tile = this.data.getTiles().get((int) pos.x(), (int) pos.z());
			if (tile == 0)
			{
				return true;
			}
		}

		return false;
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
