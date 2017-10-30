package net.jimboi.canary.stage_a.cuplet.scene_main.entity;

import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;
import net.jimboi.canary.stage_a.cuplet.scene_main.tile.TileMap;

import org.bstone.transform.Transform3;
import org.joml.Vector2f;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andy on 8/11/17.
 */
public class Explosion
{
	public static void explode(GobletWorld world, float x, float y, int power, boolean restrict, List<ExplosionUnit> dst)
	{
		int ix = (int) Math.floor(x);
		int iy = (int) Math.floor(y);

		ExplosionUnit root = new ExplosionUnit(ix, iy, power);
		dst.add(root);

		spreadAll(world, dst, restrict);

		float maxDist = power * power;
		Vector2f center = new Vector2f(root.x, root.y);
		for(ExplosionUnit explosion : dst)
		{
			float dist = center.distanceSquared(explosion.x, explosion.y);
			attemptFire(world, explosion.x + 0.5F, explosion.y + 0.5F, 1 - dist / maxDist);
		}
	}

	static class ExplosionUnit
	{
		static final int EAST = 0;
		static final int NORTH = 1;
		static final int WEST = 2;
		static final int SOUTH = 3;

		boolean dirty = true;
		int x;
		int y;
		int power;
		final boolean[] motion = new boolean[4];

		ExplosionUnit(int x, int y, int power)
		{
			this.x = x;
			this.y = y;
			this.power = power;
			this.motion[0] = this.motion[1] = this.motion[2] = this.motion[3] = true;
		}

		ExplosionUnit(ExplosionUnit unit)
		{
			this.x = unit.x;
			this.y = unit.y;
			this.power = unit.power;
			this.motion[0] = unit.motion[0];
			this.motion[1] = unit.motion[1];
			this.motion[2] = unit.motion[2];
			this.motion[3] = unit.motion[3];
		}

		static int getOffsetX(int direction)
		{
			return direction == ExplosionUnit.EAST ? 1 : direction == ExplosionUnit.WEST ? -1 : 0;
		}

		static int getOffsetY(int direction)
		{
			return direction == ExplosionUnit.NORTH ? 1 : direction == ExplosionUnit.SOUTH ? -1 : 0;
		}
	}

	static void prepare(GobletWorld world, ExplosionUnit explosion)
	{
		TileMap tilemap = world.getRoom(explosion.x, explosion.y);

		for(int direction = 0; direction < 4; ++direction)
		{
			int x = explosion.x + ExplosionUnit.getOffsetX(direction);
			int y = explosion.y + ExplosionUnit.getOffsetY(direction);
			if (tilemap.getTile(x, y).isSolid())
			{
				explosion.motion[direction] = false;
			}
		}
	}

	static void spread(GobletWorld world, Collection<ExplosionUnit> explosions, ExplosionUnit parent, boolean restrict, Collection<ExplosionUnit> dst)
	{
		parent.dirty = false;

		if (parent.power <= 1)
		{
			return;
		}

		boolean[] directions = new boolean[4];

		for(int direction = 0; direction < 4; ++direction)
		{
			directions[direction] = false;
			if (parent.motion[direction] || !restrict)
			{
				int x = parent.x + ExplosionUnit.getOffsetX(direction);
				int y = parent.y + ExplosionUnit.getOffsetY(direction);

				if (world.getRoom(x, y).getTile(x, y).isSolid()) continue;

				directions[direction] = true;
			}
		}

		for(int direction = 0; direction < 4; ++direction)
		{
			if (directions[direction])
			{
				int x = parent.x + ExplosionUnit.getOffsetX(direction);
				int y = parent.y + ExplosionUnit.getOffsetY(direction);
				int power = parent.power - 1;

				//If there already exists an explosion...
				boolean flag = false;
				for(ExplosionUnit explosion : explosions)
				{
					if (explosion.x == x && explosion.y == y)
					{
						boolean flag1 = false;
						explosion.power = (explosion.power + power) / 2;
						if (parent.motion[0] && !explosion.motion[0]) explosion.motion[0] = flag1 = true;
						if (parent.motion[1] && !explosion.motion[1]) explosion.motion[1] = flag1 = true;
						if (parent.motion[2] && !explosion.motion[2]) explosion.motion[2] = flag1 = true;
						if (parent.motion[3] && !explosion.motion[3]) explosion.motion[3] = flag1 = true;
						if (flag1)
						{
							explosion.dirty = true;
						}
						flag = true;
						break;
					}
				}

				if (!flag)
				{
					//If there exists nothing...
					ExplosionUnit child = new ExplosionUnit(parent);
					child.x = x;
					child.y = y;
					child.power = power;
					dst.add(child);
				}
			}
		}
	}

	static void spreadAll(GobletWorld world, List<ExplosionUnit> explosions, boolean restrict)
	{
		Set<ExplosionUnit> cache = new HashSet<>();
		boolean flag = false;
		while(!flag)
		{
			flag = true;
			for(int i = 0; i < explosions.size(); ++i)
			{
				ExplosionUnit explosion = explosions.get(i);
				if (explosion.dirty)
				{
					prepare(world, explosion);
					spread(world, explosions, explosion, restrict, cache);
					explosion.dirty = false;
					flag = false;

					explosions.addAll(cache);
					cache.clear();
				}
			}
		}
	}

	static void attemptFire(GobletWorld world, float x, float y, float strength)
	{
		TileMap tilemap = world.getRoom(x, y);
		if (!tilemap.getTile(x, y).isSolid())
		{
			world.spawnEntity(new EntityFire(world, new Transform3().setPosition(x, y, 0), strength));
		}
	}
}
