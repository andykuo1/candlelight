package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;

import org.qsilver.entity.Entity;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityPlayer extends EntityGlim
{
	public static Entity create(WorldGlim world)
	{
		Transform3 transform = Transform3.create();
		return MANAGER.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.25F, 0.25F))));
	}
}
