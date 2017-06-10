package net.jimboi.glim.gameentity.component;

import net.jimboi.glim.RendererGlim;
import net.jimboi.mod.instance.Instance;
import net.jimboi.mod.instance.InstanceHandler;
import net.jimboi.mod.instance.InstanceManager;
import net.jimboi.mod2.transform.Transform;

import org.qsilver.material.Material;
import org.qsilver.model.Model;

import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class GameComponentInstance extends GameComponent implements InstanceHandler
{
	private final Transform transform;
	private final String modelID;
	private final String materialID;
	private final String renderType;

	public GameComponentInstance(Transform transform, String modelID, String materialID, String renderType)
	{
		this.transform = transform;
		this.modelID = modelID;
		this.materialID = materialID;
		this.renderType = renderType;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RendererGlim.get(this.modelID);
		Material material = RendererGlim.get(this.materialID);
		instances.add(new Instance(model, material, this.renderType));
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
