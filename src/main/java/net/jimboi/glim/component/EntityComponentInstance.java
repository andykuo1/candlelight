package net.jimboi.glim.component;

import net.jimboi.glim.RendererGlim;
import net.jimboi.mod.transform.Transform;

import org.qsilver.entity.Component;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentInstance extends Component implements InstanceHandler
{
	private final Transform transform;
	private final String modelID;
	private final String materialID;

	public EntityComponentInstance(Transform transform, String modelID, String materialID)
	{
		this.transform = transform;
		this.modelID = modelID;
		this.materialID = materialID;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RendererGlim.get(this.modelID);
		Material material = RendererGlim.get(this.materialID);
		instances.add(new Instance(model, material));
	}

	@Override
	public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
	{
		instance.setTransformation(this.transform);
	}

	@Override
	public void onInstanceDestroy(InstanceManager instanceManager, Instance instance)
	{

	}

	public String getModelID()
	{
		return this.modelID;
	}

	public String getMaterialID()
	{
		return this.materialID;
	}
}
