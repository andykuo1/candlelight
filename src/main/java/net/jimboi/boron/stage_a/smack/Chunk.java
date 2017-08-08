package net.jimboi.boron.stage_a.smack;

import net.jimboi.boron.stage_a.smack.entity.EntityAmmo;
import net.jimboi.boron.stage_a.smack.entity.EntityBoulder;
import net.jimboi.boron.stage_a.smack.entity.EntityMeat;
import net.jimboi.boron.stage_a.smack.entity.EntitySpawner;
import net.jimboi.boron.stage_a.smack.entity.EntityZombie;

import org.bstone.transform.Transform3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 8/6/17.
 */
public class Chunk
{
	public static final float CHUNK_SIZE = 16;

	private final int chunkX;
	private final int chunkY;
	private long seed;

	private long lastActiveTime;

	private List<SmackEntity> entities = new ArrayList<>();

	public Chunk(SmackWorld world, int chunkX, int chunkY, long seed)
	{
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.seed = seed;

		generate(world, this, new Random(seed));
	}

	public void markDirty()
	{
		this.lastActiveTime = System.currentTimeMillis();
	}

	public long getLastActiveTime()
	{
		return this.lastActiveTime;
	}

	public List<SmackEntity> getEntities()
	{
		return this.entities;
	}

	public int getChunkX()
	{
		return this.chunkX;
	}

	public int getChunkY()
	{
		return this.chunkY;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Chunk)
		{
			return ((Chunk) o).chunkX == this.chunkX && ((Chunk) o).chunkY == this.chunkY;
		}
		return false;
	}

	private static void generate(SmackWorld world, Chunk chunk, Random random)
	{
		float posX = chunk.chunkX * CHUNK_SIZE;
		float posY = chunk.chunkY * CHUNK_SIZE;

		for(int i = 1; i < CHUNK_SIZE - 1; ++i)
		{
			chunk.entities.add(new EntityBoulder(world, new Transform3().setPosition(posX + i, posY, 0), 1));
			chunk.entities.add(new EntityMeat(world, new Transform3().setPosition(posX + i, posY + CHUNK_SIZE, 0), 0xFFFFFF));
			chunk.entities.add(new EntityMeat(world, new Transform3().setPosition(posX, posY + i, 0), 0xFFFFFF));
			chunk.entities.add(new EntityMeat(world, new Transform3().setPosition(posX + CHUNK_SIZE, posY + i, 0), 0xFFFFFF));
		}

		if (world.getRandom().nextInt(6) == 0)
		{
			chunk.entities.add(new EntitySpawner(world, new Transform3().setPosition(posX + random.nextInt(16), posY + random.nextInt(16), 0)));
		}

		for(int i = random.nextInt(5); i > 0; --i)
		{
			chunk.entities.add(new EntityAmmo(world, new Transform3().setPosition(posX + random.nextInt(16), posY + random.nextInt(16), 0)));
		}

		for(int i = random.nextInt(8); i > 0; --i)
		{
			int size = random.nextInt(8);
			chunk.entities.add(new EntityBoulder(world, new Transform3().setPosition(posX + random.nextInt(14 - size) + 1 + size, posY + random.nextInt(14 - size) + 1 + size, 0), size));
		}

		for(int i = random.nextInt(10); i > 0; --i)
		{
			chunk.entities.add(new EntityZombie(world, new Transform3().setPosition(posX + random.nextInt(16), posY + random.nextInt(16), 0)));
		}
	}
}
