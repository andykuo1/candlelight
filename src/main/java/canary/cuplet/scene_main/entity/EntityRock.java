package canary.cuplet.scene_main.entity;

import canary.cuplet.scene_main.GobletWorld;

import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityRock extends EntityThrowable
{
	public EntityRock(GobletWorld world, Transform3 transform, float dx, float dy, float dz)
	{
		super(world, transform, world.createBoundingBox(transform, 0.5F), world.createRenderable2D(transform, 'O', 0x888888), dx, dy, dz);
	}
}
