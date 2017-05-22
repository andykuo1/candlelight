package net.jimboi.dood.base;

import net.jimboi.dood.component.ComponentInstanceable;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.mod.RenderUtil;

import org.qsilver.entity.Entity;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.List;

/**
 * Created by Andy on 5/22/17.
 */
public class EntityInstanceHandler implements InstanceHandler
{
	protected final Entity entity;

	public EntityInstanceHandler(Entity entity)
	{
		this.entity = entity;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RenderUtil.getModel(this.entity.getComponent(ComponentInstanceable.class).modelID);
		Material material = RenderUtil.getMaterial(this.entity.getComponent(ComponentInstanceable.class).materialID);
		instances.add(new Instance(model, material));
	}

	@Override
	public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
	{
		if (this.entity.isDead())
		{
			instance.setDead();
		}
		else
		{
			instance.setTransformation(this.entity.getComponent(ComponentTransform.class).transform);
		}
	}

	@Override
	public void onInstanceDestroy(InstanceManager instanceManager, Instance instance)
	{
	}
}
