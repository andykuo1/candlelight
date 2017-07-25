package net.jimboi.boron.base;

import org.bstone.window.camera.Camera;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class RenderBase extends RenderService implements RenderEngine.OnRenderUpdateListener
{
	Scene scene;

	private Camera mainCamera;

	public RenderBase(Camera camera)
	{
		this.mainCamera = camera;
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
		handler.onRenderUpdate.addListener(this);
		this.onLoad(handler);
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
		handler.onRenderUpdate.deleteListener(this);
		this.onUnload(handler);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine)
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
