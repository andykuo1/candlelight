package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityThrowable extends EntityMotion
{
	public static float GRAVITY = 0.01F;

	private boolean falling;
	private float fallMotion;
	private float fallOffset;

	public EntityThrowable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, EntityComponentRenderable renderable, float dx, float dy, float dz)
	{
		super(world, transform, boundingBox, renderable);

		this.motion.set(dx, dy);
		this.fallMotion = dz;
		this.fallOffset = 0;
		this.falling = this.fallMotion > 0;
		this.friction = 0.1F;
	}

	@Override
	public void onMotionUpdate()
	{
		if (this.falling)
		{
			this.fallMotion -= GRAVITY;
			this.fallOffset += this.fallMotion;

			if (this.fallOffset < 0)
			{
				this.fallOffset = 0;
				this.falling = false;
			}
		}
		else
		{
			super.onMotionUpdate();
		}
	}

	@Override
	public void onPositionUpdate(Vector2fc offset)
	{
		super.onPositionUpdate(offset);

		if (this.falling)
		{
			this.getRenderable().getRenderModel().transformation().translation(0, this.fallOffset / 2F, 0).scale(1 + this.fallOffset / 2F, 1 + this.fallOffset / 2F, 0);
		}
	}

	public boolean isFalling()
	{
		return this.falling;
	}
}
