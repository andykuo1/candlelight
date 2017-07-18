package net.jimboi.boron.base;

import org.bstone.window.camera.Camera;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class RenderBase<S extends SceneBase> extends RenderService implements RenderEngine.OnRenderUpdateListener
{
	S scene;

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

	protected abstract Class<S> getSceneClass();

	protected final S getScene()
	{
		return this.scene;
	}

	public abstract Camera getMainCamera();
}
