package net.jimboi.stage_b.glim.system;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.bounding.Bounding;
import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.square.AABB;
import net.jimboi.stage_b.glim.gameentity.GameEntity;
import net.jimboi.stage_b.glim.gameentity.GameEntityManager;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentBounding;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.instance.InstanceManager;

import org.bstone.material.Material;
import org.joml.Vector3fc;
import org.qsilver.model.Model;
import org.qsilver.scene.Scene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 6/4/17.
 */
public class EntitySystemBounding extends EntitySystemBase implements Scene.OnSceneUpdateListener, BoundingManager.OnBoundingAddListener, BoundingManager.OnBoundingRemoveListener
{
	private Map<Bounding, Instance> instances = new HashMap<>();

	private InstanceManager instanceManager;

	public EntitySystemBounding(GameEntityManager entityManager, Scene scene, BoundingManager boundingManager, InstanceManager instanceManager)
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
		Collection<GameEntity> entities = this.entityManager.getEntitiesWithComponent(GameComponentTransform.class, GameComponentBounding.class);

		for (GameEntity entity : entities)
		{
			GameComponentTransform componentTransform = entity.getComponent(GameComponentTransform.class);
			GameComponentBounding componentBounding = entity.getComponent(GameComponentBounding.class);
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

				Vector3fc vec = bounding.position();
				inst.transformation().translation(vec.x(), vec.y() + 0.5F, vec.z()).scale(aabb.radius.x * 2, 1, aabb.radius.y * 2);
			}
		}
	}

	@Override
	public void onBoundingAdd(Bounding bounding)
	{
		if (bounding instanceof AABB)
		{
			Instance inst = new Instance(
					RendererGlim.INSTANCE.getAssetManager().getAsset(Model.class, "box"),
					RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, "box"),
					"wireframe");
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
