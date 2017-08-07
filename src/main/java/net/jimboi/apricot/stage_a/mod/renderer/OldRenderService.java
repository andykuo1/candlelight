package net.jimboi.apricot.stage_a.mod.renderer;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class OldRenderService extends RenderService
{
	public interface OnRenderUpdateListener
	{
		void onRender();
	}

	public final Listenable<OnRenderUpdateListener> onRender = new Listenable<>((listener, objects) -> listener.onRender());

	public OldRenderService(RenderEngine renderEngine)
	{
		super(renderEngine);
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		this.onRenderLoad(handler);
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		this.onRenderUnload(handler);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.onRender(renderEngine);
		this.onRender.notifyListeners();
	}

	public abstract void onRenderLoad(RenderEngine renderEngine);
	public abstract void onRender(RenderEngine renderEngine);
	public abstract void onRenderUnload(RenderEngine renderEngine);
}
