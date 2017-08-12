package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/10/17.
 */
public class EntityBomb extends EntityThrowable
{
	private final ExplosionFunction explosion;

	public EntityBomb(GobletWorld world, Transform3 transform, float dx, float dy, float dz, ExplosionFunction explosion)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.2F),
				world.createRenderable2D(transform, 'T', 0x0000FF),
				dx, dy, dz);

		this.explosion = explosion;
	}

	@Override
	public void onFallToGround()
	{
		super.onFallToGround();

		this.explosion.execute(this.world, this.transform.posX(), this.transform.posY());
		this.setDead();
	}
}
