package net.jimboi.glim.entity;

import net.jimboi.glim.WorldGlim;
import net.jimboi.glim.bounding.square.AABB;
import net.jimboi.glim.component.EntityComponentBounding;
import net.jimboi.glim.component.EntityComponentTransform;
import net.jimboi.mod2.transform.Transform3;

import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityPlayer extends EntityGlim
{
	public static Entity create(WorldGlim world)
	{
		Transform3 transform = Transform3.create();
		return MANAGER.createEntity()
				.addComponent(new EntityComponentTransform(transform))
				.addComponent(new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.25F, 0.25F))));
	}
}
