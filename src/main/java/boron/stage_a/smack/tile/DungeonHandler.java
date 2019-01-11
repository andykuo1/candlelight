package boron.stage_a.smack.tile;

import boron.base.gridmap.IntMap;
import boron.stage_a.base.collisionbox.box.GridBasedBoundingBox;
import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.smack.SmackWorld;

import boron.bstone.livingentity.LivingEntity;

import java.util.Random;

/**
 * Created by Andy on 8/7/17.
 */
public class DungeonHandler implements BoxCollider
{
	private Random random;
	private Level currentLevel;
	private GridBasedBoundingBox boundingBox;

	public DungeonHandler(SmackWorld world, long seed)
	{
		this.random = new Random();
		this.createLevel(world, seed);
	}

	public void createLevel(SmackWorld world, long seed)
	{
		this.currentLevel = LevelGenerator.generate(world, seed);
		IntMap tilemap = this.currentLevel.getTileMap();
		this.boundingBox = new GridBasedBoundingBox(0, 0, tilemap.getWidth(), tilemap.getHeight());
		for (int i = 0; i < tilemap.length(); ++i)
		{
			this.boundingBox.setSolid(i, tilemap.get(i) == 0);
		}
		for(LivingEntity entity : this.currentLevel.getEntities())
		{
			world.getSmacks().getLivingManager().addLiving(entity);
		}
	}

	public Level getCurrentLevel()
	{
		return this.currentLevel;
	}

	@Override
	public GridBasedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
