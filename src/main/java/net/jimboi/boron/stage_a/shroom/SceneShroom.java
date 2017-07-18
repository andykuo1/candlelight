package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.SceneBase;

import org.zilar.BasicFirstPersonCameraController;
import org.zilar.bounding.BoundingManager;

/**
 * Created by Andy on 7/17/17.
 */
public class SceneShroom extends SceneBase<RenderShroom>
{
	protected BasicFirstPersonCameraController cameraController;

	protected BoundingManager boundingManager;

	@Override
	protected void onSceneCreate()
	{
		this.cameraController = new BasicFirstPersonCameraController();

		this.boundingManager = new BoundingManager();
	}

	@Override
	protected void onSceneStart()
	{
		this.cameraController.start(this.getRender().getMainCamera());
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.cameraController.update(delta);
	}

	@Override
	protected void onSceneStop()
	{
		this.cameraController.stop();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.cameraController = null;
	}

	@Override
	protected Class<RenderShroom> getRenderClass()
	{
		return RenderShroom.class;
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
