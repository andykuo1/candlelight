package net.jimboi.mod3;

import net.jimboi.mod.entity.ComponentHandler;
import net.jimboi.mod3.blob.Renderer;

import org.qsilver.render.Instance;
import org.qsilver.render.InstanceManager;

import java.util.List;

/**
 * Created by Andy on 5/11/17.
 */
public class EntityPlayer extends Entity3D
{
	public EntityPlayer(float x, float y, float z)
	{
		super(x, y, z);
	}

	@Override
	public void onInstanceSetup(InstanceManager instanceManager, List<Instance> instances)
	{
		super.onInstanceSetup(instanceManager, instances);
	}

	@Override
	public void onInstanceUpdate(InstanceManager instanceManager, Instance instance)
	{
		super.onInstanceUpdate(instanceManager, instance);
		instance.transformation().translateLocal(0, -2.5F, 0);
	}

	@Override
	public void onComponentSetup(ComponentHandler componentHandler)
	{
		componentHandler.addComponent(0, new ComponentMotionControllerFirstPerson(this, Renderer.camera));
	}

	@Override
	public boolean onCreate()
	{
		return super.onCreate();
	}

	@Override
	public void onUpdate(double delta)
	{
		super.onUpdate(delta);
	}

	@Override
	public String getModelID()
	{
		return "cube";
	}

	@Override
	public String getMaterialID()
	{
		return "woodenCrate";
	}
}
