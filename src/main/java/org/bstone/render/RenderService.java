package org.bstone.render;

import org.bstone.service.Service;

/**
 * Created by Andy on 11/2/17.
 */
public abstract class RenderService implements Service
{
	RenderEngine renderEngine;

	@Override
	public final void start()
	{
		this.onRenderLoad(this.renderEngine);
	}

	@Override
	public final void stop()
	{
		this.onRenderUnload(this.renderEngine);
	}

	protected abstract void onRenderLoad(RenderEngine renderEngine);

	protected abstract void onRenderUnload(RenderEngine renderEngine);

	protected abstract void onRenderUpdate(RenderEngine renderEngine, double delta);

	public final RenderEngine getRenderEngine()
	{
		return this.renderEngine;
	}
}
