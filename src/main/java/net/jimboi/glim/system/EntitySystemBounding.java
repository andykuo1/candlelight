package net.jimboi.glim.system;

import net.jimboi.dood.system.EntitySystem;
import net.jimboi.glim.RendererGlim;
import net.jimboi.glim.bounding.Bounding;
import net.jimboi.glim.bounding.BoundingManager;
import net.jimboi.glim.bounding.square.AABB;
import net.jimboi.glim.component.EntityComponentBounding;
import net.jimboi.glim.component.EntityComponentTransform;

import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceManager;
import org.qsilver.scene.Scene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 6/4/17.
 */
public class EntitySystemBounding extends EntitySystem implements Scene.OnSceneUpdateListener, BoundingManager.OnBoundingAddListener, BoundingManager.OnBoundingRemoveListener
{
	private Map<Bounding, Instance> instances = new HashMap<>();

	private InstanceManager instanceManager;

	public EntitySystemBounding(EntityManager entityManager, Scene scene, BoundingManager boundingManager, InstanceManager instanceManager)
	{
		super(entityManager);

		this.registerListenable(scene.onSceneUpdate);
		this.registerListenable(boundingManager.onBoundingAdd);
		this.registerListenable(boundingManager.onBoundingRemove);

		this.instanceManager = instanceManager;
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
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(EntityComponentTransform.class, EntityComponentBounding.class);

		for (Entity entity : entities)
		{
			EntityComponentTransform componentTransform = entity.getComponent(EntityComponentTransform.class);
			EntityComponentBounding componentBounding = entity.getComponent(EntityComponentBounding.class);
			Vector3fc pos = componentTransform.transform.position();
			componentBounding.bounding.update(pos.x(), pos.y(), pos.z());
		}

		for (Map.Entry<Bounding, Instance> entry : this.instances.entrySet())
		{
			Bounding bounding = entry.getKey();
			if (bounding instanceof AABB)
			{
				AABB aabb = (AABB) bounding;
				Instance inst = entry.getValue();

				inst.transformation().translation(bounding.position()).scale(aabb.radius.x * 2, 1, aabb.radius.y * 2);
			}
		}
	}

	@Override
	public void onBoundingAdd(Bounding bounding)
	{
		if (bounding instanceof AABB)
		{
			Instance inst = new Instance(RendererGlim.get("model.box"), RendererGlim.get("material.box"));
			this.instances.put(bounding, inst);
			this.instanceManager.add(inst);
		}
	}

	@Override
	public void onBoundingRemove(Bounding bounding)
	{
		Instance inst = this.instances.get(bounding);
		if (inst != null)
		{
			inst.setDead();
			this.instances.remove(bounding);
		}
	}
}