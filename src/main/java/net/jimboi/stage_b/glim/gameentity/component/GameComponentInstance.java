package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.instance.InstanceHandler;
import net.jimboi.stage_b.gnome.instance.InstanceManager;
import net.jimboi.stage_b.gnome.transform.Transform;

import org.bstone.material.Material;
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
		Asset<Model> model = RendererGlim.INSTANCE.getAssetManager().getAsset(Model.class, this.modelID);
		Asset<Material> material = RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, this.materialID);
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
