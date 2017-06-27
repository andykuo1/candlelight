package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;

import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 6/4/17.
 */
public class EntitySystemBounding extends EntitySystemBase implements Scene.OnSceneUpdateListener
{
	protected final BoundingManager boundingManager;

	public EntitySystemBounding(EntityManager entityManager, Scene scene, BoundingManager boundingManager)
	{
		super(entityManager);

		this.registerListenable(scene.onSceneUpdate);

		this.boundingManager = boundingManager;
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
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(GameComponentTransform.class, GameComponentBounding.class);

		for (Entity entity : entities)
		{
			GameComponentTransform componentTransform = entity.getComponent(GameComponentTransform.class);
			GameComponentBounding componentBounding = entity.getComponent(GameComponentBounding.class);
			Vector3fc pos = componentTransform.transform.position();
			componentBounding.bounding.update(pos.x(), pos.y(), pos.z());
		}
	}
}
