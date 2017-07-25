package net.jimboi.boron.stage_a.shroom.woot;

import net.jimboi.boron.stage_a.shroom.CameraControllerShroom;
import net.jimboi.boron.stage_a.shroom.SceneShroomBase;
import net.jimboi.boron.stage_a.shroom.Shroom;

/**
 * Created by Andy on 7/17/17.
 */
public class SceneWoot extends SceneShroomBase
{
	public static void main(String[] args)
	{
		Shroom.run(SceneWoot.class, args);
	}

	@Override
	protected WorldWoot createWorld()
	{
		return new WorldWoot(this);
	}

	@Override
	protected CameraControllerShroom createMainCameraController()
	{
		return new CameraControllerShroom();
	}

	@Override
	public WorldWoot getWorld()
	{
		return (WorldWoot) super.getWorld();
	}

	@Override
	public CameraControllerShroom getMainCameraController()
	{
		return (CameraControllerShroom) super.getMainCameraController();
	}

	@Override
	protected Class<RenderWoot> getRenderClass()
	{
		return RenderWoot.class;
	}
}
