package net.jimboi.stage_b.glim.gameentity.component;

import org.qsilver.transform.Transform;
import org.zilar.instance.Instance;
import org.zilar.instance.InstanceHandler;
import org.zilar.instance.InstanceManager;
import org.zilar.model.Model;

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
