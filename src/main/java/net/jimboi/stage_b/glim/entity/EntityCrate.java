package net.jimboi.stage_b.glim.entity;

import net.jimboi.stage_b.glim.WorldGlim;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.gameentity.GameEntity;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.gnome.transform.Transform3;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityCrate extends EntityGlim
{
	public static GameEntity create(WorldGlim world, float x, float y, float z)
	{
		Transform3 transform = Transform3.create();
		transform.position.set(x, y, z);
		return MANAGER.createEntity(
				new GameComponentTransform(transform),
				new GameComponentInstance(transform, "box", "crate", "diffuse"),
				new GameComponentBounding(world.getBoundingManager().create(new AABB(transform.position.x, transform.position.z, 0.5F, 0.5F))));
	}
}
