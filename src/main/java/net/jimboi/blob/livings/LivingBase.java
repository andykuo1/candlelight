package net.jimboi.blob.livings;

import net.jimboi.blob.RenderUtil;
import net.jimboi.mod.transform.Transform;

import org.qsilver.living.Living;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Material;
import org.qsilver.render.Model;

import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class LivingBase extends Living implements InstanceHandler
{
	private final Transform transform;

	protected LivingBase(Transform transform)
	{
		this.transform = transform;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RenderUtil.getModel(this.getModelID());
		Material material = RenderUtil.getMaterial(this.getMaterialID());
		instances.add(new Instance(model, material));
	}

	@Override
	public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
	{
		if (this.isDead())
		{
			instance.setDead();
			return;
		}

		instance.setTransformation(this.transform);
	}

	@Override
	public void onInstanceDestroy(InstanceManager instanceManager, Instance instance)
	{
	}

	@Override
	public boolean onCreate()
	{
		return true;
	}

	@Override
	public void onEarlyUpdate()
	{
		super.onEarlyUpdate();
	}

	@Override
	public void onUpdate(double delta)
	{
		super.onUpdate(delta);
	}

	@Override
	public void onLateUpdate()
	{
		super.onLateUpdate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	public abstract String getModelID();
	public abstract String getMaterialID();

	public Transform transform()
	{
		return this.transform;
	}
}
