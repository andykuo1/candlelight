package net.jimboi.boron.base_ab.render;

import org.bstone.service.Service;

/**
 * Created by Andy on 7/15/17.
 */
@Deprecated
public abstract class OldishRenderService extends Service<OldRenderEngine>
{
	public OldishRenderService(OldRenderEngine renderEngine)
	{
		super(renderEngine.getRenderServices());
	}

	protected abstract void onRenderUpdate(OldRenderEngine renderEngine, double delta);
}
