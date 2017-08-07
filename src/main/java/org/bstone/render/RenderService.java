package org.bstone.render;

import org.bstone.service.Service;

/**
 * Created by Andy on 7/15/17.
 */
public abstract class RenderService extends Service<RenderEngine>
{
	public RenderService(RenderEngine renderEngine)
	{
		super(renderEngine.getRenderServices());
	}

	protected abstract void onRenderUpdate(RenderEngine renderEngine, double delta);
}
