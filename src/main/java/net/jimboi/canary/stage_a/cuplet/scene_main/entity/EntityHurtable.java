package net.jimboi.canary.stage_a.cuplet.scene_main.entity;

import net.jimboi.canary.stage_a.base.MaterialProperty;
import net.jimboi.canary.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.canary.stage_a.cuplet.basicobject.ComponentRenderable;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentDamageable;
import net.jimboi.canary.stage_a.cuplet.scene_main.entity.base.EntitySolid;
import net.jimboi.canary.stage_a.cuplet.scene_main.tick.TickCounter;

import org.bstone.gameobject.GameObjectManager;
import org.bstone.material.Material;
import org.bstone.transform.Transform3;
import org.bstone.util.ColorUtil;
import org.joml.Vector4f;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityHurtable extends EntitySolid implements IBurnable
{
	protected final int hurtColor;

	protected final TickCounter fireTicks = new TickCounter(180, true);

	protected int burnRate = 60;

	public EntityHurtable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);

		this.hurtColor = 0xFFFF0000;
	}

	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentDamageable());
	}

	@Override
	protected void onCreate(GameObjectManager gameObjectManager)
	{
		ComponentDamageable componentDamageable = this.getComponent(ComponentDamageable.class);
		componentDamageable.onDamageTick = this::onDamageTick;
		componentDamageable.setHealth(componentDamageable.getMaxHealth());
	}

	protected void onDamageTick()
	{
		ComponentDamageable componentDamageable = this.getComponent(ComponentDamageable.class);
		Material material = this.getRenderable().getRenderModel().material();
		material.setProperty(MaterialProperty.COLOR, ColorUtil.getNormalizedRGBA(ColorUtil.getColorMix(this.mainColor, this.hurtColor, 1 - componentDamageable.getDamageTicks().getProgress()), new Vector4f()));
	}

	@Override
	protected void onUpdate()
	{
		if (!this.fireTicks.isComplete())
		{
			this.fireTicks.tick();

			if (this.fireTicks.getTicks() % this.burnRate == 0)
			{
				this.getComponent(ComponentDamageable.class).damage(null, 1);
			}
		}
	}

	@Override
	public boolean canSetFire(float x, float y, float strength)
	{
		return (int) (Math.floor(this.transform.posX()) - Math.floor(x)) == 0 &&
				(int) (Math.floor(this.transform.posY()) - Math.floor(y)) == 0;
	}

	@Override
	public void setOnFire(float strength)
	{
		this.fireTicks.reset();
		this.getComponent(ComponentDamageable.class).damage(null, 2);
	}

	@Override
	public void extinguish()
	{
		this.fireTicks.setComplete();
	}

	@Override
	public boolean isOnFire()
	{
		return !this.fireTicks.isComplete();
	}
}
