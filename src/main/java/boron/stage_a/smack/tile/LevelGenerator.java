package boron.stage_a.smack.tile;

import boron.base.gridmap.ByteMap;
import boron.base.gridmap.IntMap;
import boron.stage_a.smack.SmackWorld;
import boron.stage_a.smack.entity.EntityAmmo;
import boron.stage_a.smack.entity.EntityBoulder;
import boron.stage_a.smack.entity.EntitySpawner;
import boron.stage_a.smack.entity.EntityZombie;

import boron.bstone.transform.Transform3;
import org.joml.Vector2f;
import boron.zilar.dungeon.DungeonData;
import boron.zilar.dungeon.maze.MazeDungeonBuilder;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Andy on 8/8/17.
 */
public class LevelGenerator
{
	public static Level generate(SmackWorld world, long seed)
	{
		MazeDungeonBuilder mdb = new MazeDungeonBuilder(seed, 45, 45);
		DungeonData data = mdb.bake();
		Random rand = new Random(seed);
		Level level = new Level(data.getTiles(), new ByteMap(45, 45), new ArrayList<>());
		Vector2f pos = new Vector2f();
		for(int i = 0; i < 30; ++i)
		{
			getRandomSpawn(rand, level.getTileMap(), pos);
			Transform3 transform = new Transform3();
			transform.setPosition(pos.x() + 0.5F, pos.y() + 0.5F, 0);
			level.getEntities().add(new EntityZombie(world, transform));
		}
		for(int i = 0; i < 12; ++i)
		{
			getRandomSpawn(rand, level.getTileMap(), pos);
			Transform3 transform = new Transform3();
			transform.setPosition(pos.x() + 0.5F, pos.y() + 0.5F, 0);
			level.getEntities().add(new EntityAmmo(world, transform));
		}
		for(int i = 0; i < 4; ++i)
		{
			getRandomSpawn(rand, level.getTileMap(), pos);
			Transform3 transform = new Transform3();
			transform.setPosition(pos.x() + 0.5F, pos.y() + 0.5F, 0);
			level.getEntities().add(new EntitySpawner(world, transform));
		}

		for(int i = 0; i < 3; ++i)
		{
			Transform3 transform = new Transform3();
			transform.setPosition(rand.nextFloat() * level.getTileMap().getWidth(), rand.nextFloat() * level.getTileMap().getHeight(), 1);
			level.getEntities().add(new EntityBoulder(world, transform, 1.5F));
		}

		return level;
	}

	public static Vector2f getRandomSpawn(Random rand, IntMap tilemap, Vector2f dst)
	{
		for(int i = 0; i < 100; ++i)
		{
			int x = rand.nextInt(tilemap.getWidth());
			int y = rand.nextInt(tilemap.getHeight());
			int tile = tilemap.get(x, y);
			if (tile != 0)
			{
				return dst.set(x, y);
			}
		}

		return dst;
	}
}
