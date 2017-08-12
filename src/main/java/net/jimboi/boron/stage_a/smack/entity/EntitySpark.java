package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/6/17.
 */
public class EntitySpark extends EntityMotion
{
	private float speed;

	private int maxLife;
	private int life;

	public EntitySpark(SmackWorld world, Transform3 transform, int color, float speed)
	{
		super(world, transform, 0, world.createRenderable2D(transform, '\'', ColorUtil.getColorWithBrightness(color, (world.getRandom().nextFloat() * 0.5f) + 0.5F)));

		this.getRenderable().getRenderModel().transformation().rotationZ(Transform.HALF_PI).translate(0.25F, -0.25F, 0);

		this.speed = speed + (this.world.getRandom().nextFloat() - 0.5F) * 0.1F;
		this.maxLife = 8 + this.world.getRandom().nextInt(8);
		this.life = this.maxLife;

		float rad = this.transform.eulerRadians().z();
		float dx = (float) Math.cos(rad);
		float dy = (float) Math.sin(rad);
		this.motion.set(dx, dy).mul(this.speed);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		//this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
		float scale = this.life / (float) this.maxLife;
		this.transform.setScale(scale, scale, 1);

		--this.life;
		if (this.life <= 0)
		{
			this.setDead();
		}
	}
}
