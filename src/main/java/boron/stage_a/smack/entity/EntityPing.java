package boron.stage_a.smack.entity;

import boron.stage_a.smack.SmackEntity;
import boron.stage_a.smack.SmackWorld;

import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/5/17.
 */
public class EntityPing extends SmackEntity
{
	private float progress1;
	private float progress2;

	private float speed = 0.001F;

	public EntityPing(SmackWorld world, Transform3 transform, int color)
	{
		super(world, transform, 0, world.createRenderable2D(transform, 'X', color));

		this.progress1 = 0.4F;
		this.progress2 = 1.8F;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		float scale = 0;
		if (this.progress1 > 0)
		{
			this.speed *= 1.2F;
			this.progress1 -= this.speed;
			scale = this.progress2 - this.progress1;
		}
		else if (this.progress2 > 0)
		{
			this.speed *= 1.4F;
			this.progress2 -= this.speed;
			scale = this.progress2;
		}
		else
		{
			this.setDead();
		}

		this.transform.setScale(scale, scale, 1);
	}
}
