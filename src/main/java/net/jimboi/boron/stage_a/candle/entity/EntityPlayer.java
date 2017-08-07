package net.jimboi.boron.stage_a.candle.entity;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.candle.world.WorldCandle;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 7/29/17.
 */
public class EntityPlayer extends EntityCandleBase
{
	private PlayerCameraController cameraController;

	public EntityPlayer(WorldCandle world, Transform3 transform, EntityComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.cameraController = new PlayerCameraController(this);
		this.cameraController.setTarget(this.transform);
	}

	@Override
	public boolean onCreate()
	{
		this.cameraController.start(this.getWorld().getScene().getMainCamera());
		return super.onCreate();
	}

	@Override
	public void onUpdate()
	{
		final double delta = 0.02F;
		super.onUpdate();
		this.cameraController.update(delta);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.cameraController.stop();
	}

	public PlayerCameraController getCameraController()
	{
		return this.cameraController;
	}
}
