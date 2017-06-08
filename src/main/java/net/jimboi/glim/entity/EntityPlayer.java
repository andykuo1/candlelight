package net.jimboi.glim.entity;

import net.jimboi.glim.WorldGlim;
import net.jimboi.glim.bounding.square.AABB;
import net.jimboi.glim.component.GameComponentBounding;
import net.jimboi.glim.component.GameComponentTransform;
import net.jimboi.glim.gameentity.GameEntity;
import net.jimboi.mod2.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityPlayer extends EntityGlim
{
	public static GameEntity create(WorldGlim world)
	{
		Transform3 transform = Transform3.create();
		return MANAGER.createEntity(
				new GameComponentTransform(transform),
				new GameComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.25F, 0.25F))));
	}
}
