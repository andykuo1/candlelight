package net.jimboi.canary.stage_a.lantern.newglim.entity;

import net.jimboi.canary.stage_a.lantern.newglim.WorldGlim;
import net.jimboi.canary.stage_a.lantern.newglim.bounding.square.AABB;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentBounding;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentTransform;

import org.bstone.transform.Transform3;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityPlayer extends EntityGlim
{
	public static Entity create(WorldGlim world)
	{
		Transform3 transform = new Transform3();
		return MANAGER.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.25F, 0.25F))));
	}
}
