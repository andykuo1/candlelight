package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.gameentity.component.GameComponent2D;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.gnome.transform.Transform;
import net.jimboi.stage_b.gnome.transform.Transform3Quat;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 6/18/17.
 */
public class EntitySystem2D extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	public EntitySystem2D(EntityManager entityManager, Scene scene)
	{
		super(entityManager);

		this.registerListenable(scene.onSceneUpdate);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(GameComponent2D.class, GameComponentTransform.class);
		for (Entity entity : entities)
		{
			GameComponent2D component2D = entity.getComponent(GameComponent2D.class);
			GameComponentTransform componentTransform = entity.getComponent(GameComponentTransform.class);

			Transform3Quat transform = (Transform3Quat) componentTransform.transform;
			transform.rotation.rotationXYZ(-Transform.HALF_PI, 0, 0);
		}
	}
}
