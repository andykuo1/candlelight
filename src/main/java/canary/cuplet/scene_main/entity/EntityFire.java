package canary.cuplet.scene_main.entity;

import canary.base.MaterialProperty;
import canary.base.collisionbox.collider.BoxCollider;
import canary.cuplet.scene_main.GobletEntity;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.tick.TickCounter;

import canary.bstone.material.Material;
import canary.bstone.transform.Transform3;
import canary.bstone.util.ColorUtil;
import org.joml.Vector4f;

/**
 * Created by Andy on 8/10/17.
 */
public class EntityFire extends GobletEntity implements IDamageSource
{
	private static final int EARLY_COLOR = 0xFFFFFF00;
	private static final int LATE_COLOR = 0xFFFF0000;

	private float strength;
	private int delay = 60;

	protected final TickCounter ageTicks = new TickCounter(5);

	public EntityFire(GobletWorld world, Transform3 transform, float strength)
	{
		super(world, transform, world.createRenderable2D(transform, 'F', ColorUtil.getColorMix(EARLY_COLOR, LATE_COLOR, world.getRandom().nextFloat() * 0.6F)));

		this.strength = strength;
		this.ageTicks.setTicks(-(int) (((1 - this.strength) * this.delay) * 0.5F));

		this.transform.setScale(0, 0, 1);
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		this.ageTicks.tick();

		final int ticks = this.ageTicks.getTicks();

		if (ticks > this.ageTicks.getMaxTicks() * 3)
		{
			this.setDead();
		}
		if (ticks > this.ageTicks.getMaxTicks() * 2)
		{
			float scale = (3 - this.ageTicks.getProgress());
			this.transform.setScale(scale, scale, 1);
		}
		else if (ticks > this.ageTicks.getMaxTicks())
		{

		}
		else if (ticks == this.ageTicks.getMaxTicks())
		{
			for(BoxCollider collider : this.world.getBoundingManager().getNearestColliders(this.world.getColliders().iterator(), this.transform.posX(), this.transform.posY(), 0.5F))
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

			Material material = this.getRenderable().getRenderModel().material();
			material.setProperty(MaterialProperty.COLOR,
					ColorUtil.getNormalizedRGBA(
							ColorUtil.getColorMix(
									ColorUtil.getColor(material.getProperty(MaterialProperty.COLOR)),
									LATE_COLOR, 0.5F), new Vector4f()));
		}
		else if (ticks > 0)
		{
			final float scale = this.ageTicks.getProgress();
			this.transform.setScale(scale, scale, 1);
			//PropertyColor.setColor(this.getRenderable().getRenderModel().getMaterial(), ColorUtil.getColorMix(EARLY_COLOR, LATE_COLOR, scale));
		}
	}
}
