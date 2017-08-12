package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityParticleBouncy extends EntityMotion
{
	private float speed;

	private boolean falling;
	private float motionFall = 0;
	private float fallOffset = 0;
	private float gravity = 0.01F;

	private float initScale = 0;

	public EntityParticleBouncy(SmackWorld world, Transform3 transform, float size, ComponentRenderable renderable)
	{
		super(world, transform, size, renderable);

		this.speed = 0.1F + (this.world.getRandom().nextFloat() - 0.5F) * 0.05F;

		float rad = this.world.getRandom().nextFloat() * Transform.PI2;
		float dx = (float) Math.cos(rad);
		float dy = (float) Math.sin(rad);
		this.motion.set(dx, dy).mul(this.speed);
		this.friction = 0.1F;

		this.motionFall = (this.world.getRandom().nextFloat() * 0.05F) + 0.05F;
		this.falling = true;

		this.initScale = this.world.getRandom().nextFloat() * 0.3F + 0.7F;
		this.transform.setScale(this.initScale, this.initScale, 1);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (this.falling)
		{
			if (this.motionFall > -0.1F)
			{
				this.motionFall -= this.gravity;
			}
			this.fallOffset += this.motionFall;
			if (this.fallOffset <= -0.1F)
			{
				this.motionFall = 0;
				this.falling = false;
			}
		}

		this.transform.translate(0, this.motionFall, 0);
		float scale = this.initScale + this.fallOffset / 4;
		this.transform.setScale(scale, scale, 1);
	}
}
