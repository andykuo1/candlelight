package canary.cuplet.scene_main.entity;

import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.tick.TickCounter;

import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 8/10/17.
 */
public class EntityGrenade extends EntityThrowable
{
	private final ExplosionFunction explosion;

	protected final TickCounter ageTicks = new TickCounter(200);

	public EntityGrenade(GobletWorld world, Transform3 transform, float dx, float dy, float dz, ExplosionFunction explosion)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.2F),
				world.createRenderable2D(transform, 'T', 0x0000FF),
				dx, dy, dz);

		this.explosion = explosion;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		this.ageTicks.tick();
		if (this.ageTicks.isComplete())
		{
			if (!this.isFalling())
			{
				this.explosion.execute(this.world, this.transform.posX(), this.transform.posY());
				this.setDead();
			}
		}
	}

	@Override
	public void onFallToGround()
	{
		super.onFallToGround();

		this.boundingBox.setHalfSize(0.3F, 0.3F);
	}
}
