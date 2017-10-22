package net.jimboi.apricot.stage_a.mod.renderer;

import net.jimboi.boron.base_ab.render.OldRenderEngine;
import net.jimboi.boron.base_ab.render.OldishRenderService;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class OldRenderService extends OldishRenderService
{
	public interface OnRenderUpdateListener
	{
		void onRender();
	}

	public final Listenable<OnRenderUpdateListener> onRender = new Listenable<>((listener, objects) -> listener.onRender());

	public OldRenderService(OldRenderEngine renderEngine)
	{
		super(renderEngine);
	}

	@Override
	protected void onServiceStart(OldRenderEngine handler)
	{
		this.onRenderLoad(handler);
	}

	@Override
	protected void onServiceStop(OldRenderEngine handler)
	{
		this.onRenderUnload(handler);
	}

	@Override
	protected void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{
		this.onRender(renderEngine);
		this.onRender.notifyListeners();
	}

	public abstract void onRenderLoad(OldRenderEngine renderEngine);
	public abstract void onRender(OldRenderEngine renderEngine);
	public abstract void onRenderUnload(OldRenderEngine renderEngine);
}
