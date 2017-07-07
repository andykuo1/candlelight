package net.jimboi.stage_b.glim.entity.system;

import net.jimboi.stage_b.glim.entity.component.EntityComponent2D;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.transform.Transform;
import org.zilar.transform.Transform3Quat;

import java.util.Collection;

/**
 * Created by Andy on 6/18/17.
 */
public class EntitySystem2D extends AbstractUpdateableSystem
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

			Transform3Quat transform = (Transform3Quat) componentTransform.transform;
			transform.rotation.rotationXYZ(-Transform.HALF_PI, 0, 0);
		}
	}
}
