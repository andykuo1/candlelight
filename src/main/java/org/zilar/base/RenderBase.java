package org.zilar.base;

import net.jimboi.stage_a.mod.renderer.OldRenderService;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.qsilver.render.RenderEngine;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class RenderBase extends OldRenderService
{
	protected SceneBase scene;

	private final Camera camera;

	public RenderBase(Camera camera, CameraController controller)
	{
		this.camera = camera;
		this.camera.setCameraController(controller);
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
	}

	@Override
	public void onRender(RenderEngine renderEngine)
	{
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
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
