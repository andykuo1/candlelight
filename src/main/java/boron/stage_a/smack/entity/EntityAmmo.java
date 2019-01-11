package boron.stage_a.smack.entity;

import boron.stage_a.smack.SmackWorld;

import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityAmmo extends EntityParticleBouncy
{
	private int age;
	private float pulseSpeed;
	private int amount = 5;

	public EntityAmmo(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 0.5F, world.createRenderable2D(transform, '%', 0x0000FF));

		this.pulseSpeed = this.world.getRandom().nextFloat() * 20F + 10F;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.age++;
		float scale = (float) Math.cos(this.age / this.pulseSpeed) * 0.1F + 0.9F;
		this.transform.setScale(scale, scale, 1);
	}

	public int getAmount()
	{
		return this.amount;
	}
}
