package net.jimboi.boron.stage_a.shroom.component;

import net.jimboi.boron.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.base.livingentity.EntityComponentTransform;
import net.jimboi.boron.stage_a.shroom.woot.WorldWoot;
import net.jimboi.boron.stage_a.shroom.woot.collision.DynamicCollider;

import org.bstone.living.Living;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/17/17.
 */
public class OldLivingEntity extends Living implements EntityComponent
{
	private final WorldWoot world;
	private final Transform3 transform;
	private final EntityComponentRenderable renderable;

	private final DynamicCollider collider;

	public OldLivingEntity(WorldWoot world, Transform3 transform, DynamicCollider collider, EntityComponentRenderable renderable)
	{
		this.world = world;
		this.transform = transform;
		this.collider = collider;
		this.renderable = renderable;

		if (this.collider != null)
		{
			this.collider.setPosition(this.transform.position.x(), this.transform.position.z());
		}
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	public void onEntityCreate(Entity entity)
	{
		entity.addComponent(new EntityComponentTransform(this.transform));
		if (this.collider != null)
		{
			entity.addComponent(new EntityComponentBounding(this.collider));
		}
		if (this.renderable != null)
		{
			entity.addComponent(this.renderable);
		}
	}

	public void onEntityDestroy(Entity entity)
	{
	}

	public WorldWoot getWorld()
	{
		return this.world;
	}

	public Transform3c getTransform()
	{
		return this.transform;
	}

	public DynamicCollider getCollider()
	{
		return this.collider;
	}

	public EntityComponentRenderable getRenderable()
	{
		return this.renderable;
	}
}
