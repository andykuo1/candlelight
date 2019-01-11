package apricot.stage_b.glim.entity.system;

import apricot.base.entity.AbstractUpdateableEntitySystem;
import apricot.base.entity.Entity;
import apricot.base.entity.EntityManager;
import apricot.stage_b.glim.entity.component.EntityComponent2D;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;

import apricot.bstone.transform.Transform;
import apricot.bstone.transform.Transform3;

import java.util.Collection;

/**
 * Created by Andy on 6/18/17.
 */
public class EntitySystem2D extends AbstractUpdateableEntitySystem
{
	public EntitySystem2D(EntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(EntityComponent2D.class, EntityComponentTransform.class);
		for (Entity entity : entities)
		{
			EntityComponent2D component2D = entity.getComponent(EntityComponent2D.class);
			EntityComponentTransform componentTransform = entity.getComponent(EntityComponentTransform.class);

			Transform3 transform = (Transform3) componentTransform.transform;
			transform.rotation.rotationXYZ(-Transform.HALF_PI, 0, 0);
		}
	}
}
