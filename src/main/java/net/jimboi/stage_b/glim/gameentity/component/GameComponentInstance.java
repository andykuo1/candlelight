package net.jimboi.stage_b.glim.gameentity.component;

import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.instance.InstanceHandler;
import net.jimboi.stage_b.gnome.instance.InstanceManager;
import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.transform.Transform;

import java.util.List;

/**
 * Created by Andy on 6/1/17.
 */
public class GameComponentInstance extends GameComponent implements InstanceHandler
{
	private final Transform transform;
	private final Model model;

	public GameComponentInstance(Transform transform, Model model)
	{
		this.transform = transform;
		this.model = model;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		instances.add(new Instance(this.model));
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

	public Model getModel()
	{
		return this.model;
	}
}
