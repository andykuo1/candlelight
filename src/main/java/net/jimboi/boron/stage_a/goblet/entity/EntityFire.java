package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.goblet.GobletEntity;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.render.material.PropertyColor;
import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/10/17.
 */
public class EntityFire extends GobletEntity implements IDamageSource
{
	private static final int EARLY_COLOR = 0xFFFF00;
	private static final int LATE_COLOR = 0xFF0000;

	private float strength;
	private int delay = 60;
	private int maxAge = 10;
	private int age;

	public EntityFire(GobletWorld world, Transform3 transform, float strength)
	{
		super(world, transform, world.createRenderable2D(transform, 'F', ColorUtil.getColorMix(EARLY_COLOR, LATE_COLOR, world.getRandom().nextFloat() * 0.6F)));

		this.strength = strength;
		this.age = -(int) ((1 - this.strength) * this.delay);

		this.transform.setScale(0, 0, 1);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		++this.age;
		if (this.age > this.maxAge * 3)
		{
			this.setDead();
		}
		if (this.age > this.maxAge * 2)
		{
			float scale = (3 - this.age / (float) this.maxAge);
			this.transform.setScale(scale, scale, 1);
		}
		else if (this.age > this.maxAge)
		{

		}
		else if (this.age == this.maxAge)
		{
			for(BoxCollider collider : this.world.getBoundingManager().getNearestColliders(this.transform.posX(), this.transform.posY(), 0.5F))
			{
				if (collider instanceof IBurnable)
				{
					IBurnable burnable = (IBurnable) collider;
					if (burnable.canSetFire(this.transform.posX(), this.transform.posY(), this.strength))
					{
						burnable.setOnFire(this.strength);
					}
				}
			}

			PropertyColor.PROPERTY.bind(this.getRenderable().getRenderModel().getMaterial())
					.setColor(ColorUtil.getColorMix(
							PropertyColor.PROPERTY.getColor(this.getRenderable().getRenderModel().getMaterial()),
							LATE_COLOR, 0.5F))
					.unbind();
		}
		else if (this.age > 0)
		{
			float scale = (this.age / (float) this.maxAge);
			this.transform.setScale(scale, scale, 1);
			//PropertyColor.setColor(this.getRenderable().getRenderModel().getMaterial(), ColorUtil.getColorMix(EARLY_COLOR, LATE_COLOR, scale));
		}
	}
}
