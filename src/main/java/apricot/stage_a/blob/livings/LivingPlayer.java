package apricot.stage_a.blob.livings;

import apricot.stage_a.blob.RendererBlob;
import apricot.stage_a.mod.instance.Instance;
import apricot.stage_a.mod.instance.InstanceManager;

import java.util.List;

/**
 * Created by Andy on 5/11/17.
 */
public class LivingPlayer extends LivingMotionControllerFirstPerson
{
	public LivingPlayer(float x, float y, float z)
	{
		super(x, y, z, RendererBlob.camera);
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
	public boolean onCreate()
	{
		return super.onCreate();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
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

	@Override
	public String getRenderType()
	{
		return "lit";
	}
}
