package boron.stage_a.smack.entity;

import boron.stage_a.smack.SmackEntity;
import boron.stage_a.smack.SmackWorld;

import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityBoulder extends SmackEntity
{
	public EntityBoulder(SmackWorld world, Transform3 transform, float size)
	{
		super(world, transform, size, world.createRenderable2D(transform, 'O', 0x888888));

		this.renderable.getRenderModel().transformation().translate(1 / 16F, -1 / 16F, 0).scale(size * 1.25F, size * 1.25F, 1);
	}
}
