package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityMeat extends EntityParticleBouncy
{
	private int maxLife;
	private int life;

	public EntityMeat(SmackWorld world, Transform3 transform, int color)
	{
		super(world, transform, 0.5F, world.createRenderable2D(transform, '&', ColorUtil.getColorWithBrightness(color, 0.5f + 0.5F * world.getRandom().nextFloat())));

		this.maxLife = 60 + this.world.getRandom().nextInt(40);
		this.life = this.maxLife;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		--this.life;
		if (this.life <= 0)
		{
			this.setDead();
		}
	}
}
