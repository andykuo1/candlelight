package net.jimboi.glim.entity;

import net.jimboi.glim.WorldGlim;
import net.jimboi.glim.bounding.square.AABB;
import net.jimboi.glim.component.EntityComponentBounding;
import net.jimboi.glim.component.EntityComponentInstance;
import net.jimboi.glim.component.EntityComponentTransform;
import net.jimboi.mod.transform.Transform3;

import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityCrate extends EntityGlim
{
	public static Entity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = Transform3.create();
		transform.position.set(x, y, z);
		return MANAGER.createEntity()
				.addComponent(new EntityComponentTransform(transform))
				.addComponent(new EntityComponentInstance(transform, "model.box", "material.crate"))
				.addComponent(new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
