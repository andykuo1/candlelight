package net.jimboi.boron.stage_a.smack.tile;

import org.bstone.livingentity.LivingEntity;
import org.qsilver.util.map2d.ByteMap;
import org.qsilver.util.map2d.IntMap;

import java.util.List;

/**
 * Created by Andy on 8/8/17.
 */
public class Level
{
	public final IntMap tilemap;
	public final ByteMap metamap;
	public final List<LivingEntity> entities;

	public Level(IntMap tilemap, ByteMap metamap, List<LivingEntity> entities)
	{
		this.tilemap = tilemap;
		this.metamap = metamap;
		this.entities = entities;
	}

	public IntMap getTileMap()
	{
		return this.tilemap;
	}

	public ByteMap getMetaMap()
	{
		return this.metamap;
	}

	public List<LivingEntity> getEntities()
	{
		return this.entities;
	}
}
