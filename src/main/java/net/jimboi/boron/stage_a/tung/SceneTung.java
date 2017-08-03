package net.jimboi.boron.stage_a.tung;

import net.jimboi.boron.base.SceneLivingBase;

import org.bstone.transform.Transform3;
import org.bstone.window.camera.CameraController;
import org.zilar.BasicSideScrollCameraController;

/**
 * Created by Andy on 7/23/17.
 */
public class SceneTung extends SceneLivingBase
{
	private CameraController mainCameraController;
	private WorldTung world;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.mainCameraController = new BasicSideScrollCameraController();
		this.mainCameraController.setTarget(new Transform3());

		this.world = new WorldTung(this);
	}

	@Override
	protected void onSceneStart()
	{
		this.mainCameraController.start(this.getRender().getMainCamera());

		this.startService(this.world);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		super.onSceneUpdate(delta);

		this.mainCameraController.update(delta);
	}

	@Override
	protected void onSceneStop()
	{
		super.onSceneStop();

		this.stopService(this.world);

		this.mainCameraController.stop();
	}

	public WorldTung getWorld()
	{
		return this.world;
	}

	public CameraController getMainCameraController()
	{
		return this.mainCameraController;
	}

	@Override
	protected Class<RenderTung> getRenderClass()
	{
		return RenderTung.class;
	}

	@Override
	protected RenderTung getRender()
	{
		return (RenderTung) super.getRender();
	}
}
