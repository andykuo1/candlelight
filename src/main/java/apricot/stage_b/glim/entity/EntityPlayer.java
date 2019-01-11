package apricot.stage_b.glim.entity;

import apricot.base.entity.Entity;
import apricot.stage_b.glim.WorldGlim;
import apricot.stage_b.glim.bounding.square.AABB;
import apricot.stage_b.glim.entity.component.EntityComponentBounding;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;

import apricot.bstone.transform.Transform3;

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
