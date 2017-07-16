package net.jimboi.stage_a.mod.renderer;

import org.bstone.util.listener.Listenable;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.RenderService;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class OldRenderService extends RenderService implements RenderEngine.OnRenderUpdateListener
{
	public interface OnRenderUpdateListener
	{
		void onRender();
	}

	public final Listenable<OnRenderUpdateListener> onRender = new Listenable<>((listener, objects) -> listener.onRender());

	@Override
	public void onRenderUpdate(RenderEngine renderEngine)
	{
		this.onRender(renderEngine);
		this.onRender.notifyListeners();
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
		this.onRenderLoad(handler);
		handler.onRenderUpdate.addListener(this);
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
		handler.onRenderUpdate.deleteListener(this);
		this.onRenderUnload(handler);
	}

	public abstract void onRenderLoad(RenderEngine renderEngine);
	public abstract void onRender(RenderEngine renderEngine);
	public abstract void onRenderUnload(RenderEngine renderEngine);
}
