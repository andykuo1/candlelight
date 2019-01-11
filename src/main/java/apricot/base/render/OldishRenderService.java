package apricot.base.render;

import apricot.base.service.Service;

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
