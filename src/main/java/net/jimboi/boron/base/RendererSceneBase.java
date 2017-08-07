package net.jimboi.boron.base;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.window.camera.Camera;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class RendererSceneBase extends RenderService
{
	Scene scene;

	private Camera mainCamera;

	public RendererSceneBase(RenderEngine renderEngine, Camera camera)
	{
		super(renderEngine);
		this.mainCamera = camera;
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		this.onLoad(handler);
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		this.onUnload(handler);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.onRender(renderEngine);
	}

	protected abstract void onLoad(RenderEngine renderEngine);

	protected abstract void onRender(RenderEngine renderEngine);

	protected abstract void onUnload(RenderEngine renderEngine);

	public Camera getMainCamera()
	{
		return this.mainCamera;
	}

	protected Scene getScene()
	{
		return this.scene;
	}
}
