package org.zilar.base;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.qsilver.renderer.RenderManager;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class RenderBase extends RenderManager
{
	protected SceneBase scene;

	private final Camera camera;

	public RenderBase(Camera camera, CameraController controller)
	{
		this.camera = camera;
		this.camera.setCameraController(controller);
	}

	@Override
	public void onRenderLoad()
	{
	}

	@Override
	public void onRenderUpdate()
	{
	}

	@Override
	public void onRenderUnload()
	{
	}

	protected void setScene(SceneBase scene)
	{
		this.scene = scene;
	}

	public SceneBase getScene()
	{
		return this.scene;
	}

	public Camera getCamera()
	{
		return this.camera;
	}
}
