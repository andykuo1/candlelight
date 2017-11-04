package org.bstone.render;

import org.bstone.service.Service;

/**
 * Created by Andy on 11/2/17.
 */
public abstract class RenderService implements Service<RenderEngine>
{
	@Override
	public final void start(RenderEngine handler)
	{
		this.onRenderLoad(handler);
	}

	@Override
	public final void stop(RenderEngine handler)
	{
		this.onRenderUnload(handler);
	}

	protected abstract void onRenderLoad(RenderEngine renderEngine);

	protected abstract void onRenderUnload(RenderEngine renderEngine);

	protected abstract void onRenderUpdate(RenderEngine renderEngine, double delta);
}
