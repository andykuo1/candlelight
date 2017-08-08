package net.jimboi.boron.stage_a.smack.tile;

import net.jimboi.boron.stage_a.smack.aabb.GridAlignedBoundingBox;
import net.jimboi.boron.stage_a.smack.aabb.GridBasedBoundingMap;
import net.jimboi.boron.stage_a.smack.aabb.GridCollider;

import org.qsilver.util.map2d.IntMap;

import java.util.Random;

/**
 * Created by Andy on 8/7/17.
 */
public class DungeonHandler implements GridCollider
{
	private IntMap tilemap = new IntMap(16, 16);
	private GridBasedBoundingMap gridBox;

	public DungeonHandler()
	{
		Random rand = new Random();
		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.tilemap.set(i, 0);
		}
		this.tilemap.set(4, 4, 1);
		this.tilemap.set(4, 5, 1);
		this.tilemap.set(4, 6, 1);
		this.tilemap.set(4, 7, 1);
		this.tilemap.set(4, 8, 1);

		this.gridBox = new GridBasedBoundingMap(0, 0, this.tilemap.width, this.tilemap.height);
		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.gridBox.setSolid(i, this.tilemap.get(i) != 0);
		}
	}

	public boolean intersects(float x, float y)
	{
		if (x < 0 || y < 0 || x > this.tilemap.width || y > this.tilemap.height) return true;
		return this.tilemap.get((int) Math.floor(x), (int) Math.floor(y)) == 1;
	}

	public IntMap getTiles()
	{
		return this.tilemap;
	}

	@Override
	public GridBasedBoundingMap getGridBox()
	{
		return this.gridBox;
	}

	private GridAlignedBoundingBox boundingBox = new GridAlignedBoundingBox(0, 0, 1, 1);

	@Override
	public GridAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
