package net.jimboi.apricot.stage_a.blob.livings;

import net.jimboi.apricot.stage_a.blob.RenderUtil;
import net.jimboi.apricot.stage_a.mod.ModMaterial;
import net.jimboi.apricot.stage_a.mod.instance.Instance;
import net.jimboi.apricot.stage_a.mod.instance.InstanceHandler;
import net.jimboi.apricot.stage_a.mod.instance.InstanceManager;
import net.jimboi.apricot.stage_a.mod.model.Model;

import org.bstone.living.Living;
import org.bstone.transform.Transform3;

import java.util.List;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class LivingBase extends Living implements InstanceHandler
{
	private final Transform3 transform;

	protected LivingBase(Transform3 transform)
	{
		this.transform = transform;
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		Model model = RenderUtil.getModel(this.getModelID());
		ModMaterial material = RenderUtil.getMaterial(this.getMaterialID());
		instances.add(new Instance(model, material, getRenderType()));
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

	public abstract String getRenderType();

	public Transform3 transform()
	{
		return this.transform;
	}
}
