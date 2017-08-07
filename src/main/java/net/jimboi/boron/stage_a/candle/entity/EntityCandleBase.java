package net.jimboi.boron.stage_a.candle.entity;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.candle.world.WorldCandle;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 7/29/17.
 */
public class EntityCandleBase extends LivingEntity
{
	private final WorldCandle world;

	public EntityCandleBase(WorldCandle world, Transform3 transform, EntityComponentRenderable renderable)
	{
		super(transform, renderable);

		this.world = world;
	}

	public WorldCandle getWorld()
	{
		return this.world;
	}
}
