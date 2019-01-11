package apricot.stage_a.dood.base;

import apricot.stage_a.dood.Resources;
import apricot.stage_a.dood.component.ComponentInstanceable;
import apricot.stage_a.dood.component.ComponentTransform;
import apricot.stage_a.dood.entity.Entity;
import apricot.stage_a.mod.ModMaterial;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.instance.InstanceHandler;
import apricot.stage_a.mod.instance.InstanceManager;
import apricot.stage_a.mod.model.Model;

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
		Model model = Resources.getModel(this.entity.getComponent(ComponentInstanceable.class).modelID);
		ModMaterial material = Resources.getMaterial(this.entity.getComponent(ComponentInstanceable.class).materialID);
		instances.add(new Instance(model, material, this.entity.getComponent(ComponentInstanceable.class).renderType));
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
