package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.gnome.transform.Transform3;

import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityPlayer extends EntityGlim
{
	public static Entity create(WorldGlim world)
	{
		Transform3 transform = Transform3.create();
		return MANAGER.createEntity(
				new GameComponentTransform(transform),
				new GameComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.25F, 0.25F))));
	}
}
